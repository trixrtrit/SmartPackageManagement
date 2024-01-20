package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.NotificationDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationAssembler {
    public static NotificationDTO from (Notification notification){
        return new NotificationDTO(
                notification.getId(),
                notification.getText(),
                notification.getTimestamp(),
                UserAssembler.from(notification.getUser())
        );
    }

    public static List<NotificationDTO> from(List<Notification> notifications) {
        return notifications.stream().map(NotificationAssembler::from).collect(Collectors.toList());
    }
}
