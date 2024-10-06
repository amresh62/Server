package com.kamjritztex.aoms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamjritztex.aoms.models.Notification;
import com.kamjritztex.aoms.models.OffboardingProcess;
import com.kamjritztex.aoms.repositories.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void notifyHR(OffboardingProcess process) {
        Notification notification = new Notification();
        notification.setMessage("New offboarding process initiated for " + process.getEmployee().getFirstName().toUpperCase() + " " + process.getEmployee().getLastName().toUpperCase()+" Email : "+ process.getEmployee().getEmail() + ".");
        notification.setRead(false);
        notificationRepository.save(notification);
    }
    public void createNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}