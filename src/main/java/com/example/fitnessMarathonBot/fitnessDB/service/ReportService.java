package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.MyFileWriter;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListUserGoals;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhoto;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListUserGoalsRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserPhotoRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ListUserGoalsRepository listUserGoals;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private ListUserGoalsService listUserGoalsService;

    public String getReports() {
        List<UserPhoto> userPhotos = null;
        String report = "Отчет за ";
        int quantityPhotos = 0;
        int quantityTasks = 0;
        int i = 0;
        List<ListUserGoals> userGoals = null;
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        if (userProfiles.size() > 0) {
            for (UserProfile profile : userProfiles) {
                i++;
                userPhotos = userPhotoRepository.findUserPhotoByUser(profile.getPk().getUser());
                userGoals = listUserGoals.findListUserGoalsByUser(profile.getPk().getUser());
                report = report.concat("\n\n" + " Дней на марафоне: " + userPhotos.size() + "\n");
                report = report.concat(i + "." + profile.getFullName() + ":\n");
                for (UserPhoto photo : userPhotos) {
                    quantityPhotos += userPhotoService.getCountPhotos(photo);
                }
                report = report.concat("  - Всего фото отправлено(еда): " + quantityPhotos + " из " + userPhotos.size() * 3 + ".\n");
                for (ListUserGoals listUserGoal : userGoals) {
                    quantityTasks += listUserGoalsService.countDoneTasks(listUserGoal);
                }
                report = report.concat("  - Всего выполнено заданий: " + quantityTasks + " из " + userGoals.size() * 6 + "\n");
                quantityPhotos = 0;
                quantityTasks = 0;
            }
        }

        return report;
    }

    public void getDetailedReport() {
        List<UserPhoto> userPhotos = null;
        String report = "";
        int i = 0;
        List<ListUserGoals> userGoals = null;
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        if (userProfiles.size() > 0) {
            for (UserProfile profile : userProfiles) {
                i++;
                userPhotos = userPhotoRepository.findUserPhotoByUser(profile.getPk().getUser());
                userGoals = listUserGoals.findListUserGoalsByUser(profile.getPk().getUser());
                report = report.concat("--------------------------------------\n");
                report = report.concat(i + "." + profile.getFullName() + ":\n");
                for (int k = 0; k < userPhotos.size(); k++) {
//                    if (userGoals.size()-1 > k) {
//                        continue;
//                    } else {
                        report = report.concat("Дата: " + userPhotos.get(k).getTimeStamp() + "\n");
                        report = report.concat("Фото(еда): " + userPhotoService.getCountPhotos(userPhotos.get(k)) + " из 3.\n");
                        report = report.concat("Выполнено заданий: " + listUserGoalsService.countDoneTasks(userGoals.get(k)) + " из 6.\n\n");
//                    }
                }
//                report = report.concat("  - Всего фото отправлено(еда): " + quantityPhotos + " из " + userPhotos.size() * 3 + ".\n");
//                for (ListUserGoals listUserGoal : userGoals) {
//                    quantityTasks += listUserGoalsService.countDoneTasks(listUserGoal);
//                }
//                report = report.concat("  - Всего выполнено заданий: " + quantityTasks + " из " + userGoals.size() * 6 + "\n");
            }
        }
        MyFileWriter.writeToFile(report);
    }

}
