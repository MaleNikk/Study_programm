package com.example.web.news.controller;

import com.example.web.news.entity.MassageEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.entity.RoleEntity;
import com.example.web.news.injections.service.ServiceMassage;
import com.example.web.news.injections.service.ServiceReaders;
import com.example.web.news.security.injection.NewsAppSecurityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ReadersController {

    private final ServiceReaders<ReaderEntity> serviceReaders;

    private final NewsAppSecurityService securityService;

    private final ServiceMassage serviceMassage;

    @GetMapping("/readers/registration")
    public String registration(Model model) {
        ReaderEntity readerEntity = new ReaderEntity();
        model.addAttribute("readerEntity", readerEntity);
        return "readers/registration";
    }

    @PostMapping("/readers/registration")
    public String completeRegistration(@ModelAttribute ReaderEntity readerEntity, Model model) {
        AtomicLong atomicLong = new AtomicLong(getServiceReaders().searchAll().size());
        long id;
        do {
            id = atomicLong.getAndIncrement();
        } while (getServiceReaders().numbersId().contains(id));
        getServiceReaders().numbersId().add(id);
        readerEntity.setId(id);
        getServiceReaders().save(readerEntity);
        model.addAttribute("readers", getServiceReaders().searchAll());
        return "redirect:/";
    }

    @GetMapping("/readers/management")
    public String managementConnect(Model model) {
        ReaderEntity readerEntity = new ReaderEntity();
        model.addAttribute("readerEntity", readerEntity);
        return "readers/checkRole";

    }

    @PostMapping("/readers/checkRole")
    public String managementContinueConnect(@ModelAttribute ReaderEntity readerEntity, Model model) {
        log.info("Init method check role in class ReadersController.");
        ReaderEntity checkEntity = serviceReaders.searchAll()
                .stream()
                .filter(reader -> (readerEntity.getName().equals(reader.getName())
                        && readerEntity.getSurname().equals(reader.getSurname())
                        && readerEntity.getPin().equals(reader.getPin())))
                .findFirst().orElse(null);
        if (checkEntity != null) {
            Long pin = checkEntity.getPin();
            if (securityService.checkRoleUser(pin)) {
                model.addAttribute("readers", List.of(checkEntity));
                return "readers/showReaders";
            }
            if (securityService.checkRoleModerator(pin)) {
                model.addAttribute("readers", getServiceReaders().searchAll()
                        .stream()
                        .filter(reader -> (getSecurityService().checkRoleUser(reader.getPin())) ||
                        getSecurityService().checkRoleModerator(reader.getPin())));
                return "readers/showReadersModerator";
            }
            if (securityService.checkRoleAdmin(pin)) {
                model.addAttribute("readers", getServiceReaders().searchAll());
                model.addAttribute("massages", getServiceMassage().getAll());
                return "readers/showReadersAdmin";
            }
        }
        return "security/checkRegistration";
    }

    @GetMapping("/readers/editReader/{id}")
    public String editReader(@PathVariable Long id, Model model) {
        log.info("Init method editReader in class ReadersController.");
        ReaderEntity readerEntity = getServiceReaders().searchById(id);
        if (readerEntity == null) {
            return "redirect:/";
        } else {
            model.addAttribute("readerEntity", readerEntity);
            return "/readers/editReader";
        }
    }

    @PostMapping("/readers/editReader")
    public String completeEditReader(@ModelAttribute ReaderEntity readerEntity) {
        log.info("Init method completeEditReader in class ReadersController.");
        getServiceReaders().edit(readerEntity);
        return "redirect:/";
    }

    @GetMapping("/readers/editRoleReader/{id}")
    public String editRoleReader(@PathVariable Long id, Model model) {
        log.info("Init method editRoleReader in class ReadersController.");
        RoleEntity roleEntity = new RoleEntity(id, " ");
        model.addAttribute("roleEntity", roleEntity);
        return "/readers/editRoleReader";
    }

    @PostMapping("/readers/editRoleReader")
    public String completeEditRoleReader(@ModelAttribute RoleEntity roleEntity) {
        log.info("Init method completeEditRoleReader in class ReadersController.");
        getSecurityService().refactorRole(roleEntity.getUser_id(),roleEntity.getRole());
        return "redirect:/";
    }


    @GetMapping("/readers/deleteReader/{id}")
    public String removeReader(@PathVariable Long id, Model model) {
        log.info("Init method removeReader in class ReadersController.");
        getServiceReaders().removeById(id);
        model.addAttribute("readers", getServiceReaders().searchAll());
        return "redirect:/";
    }

    @GetMapping("/comeback")
    public String comeBack() {
        log.info("Init method comeback in class ReadersController.");
        return "redirect:/";
    }

    @GetMapping("/readers/questions")
    public String managementMassage(Model model) {
        log.info("Init method managementMassage in class ReadersController.");
        model.addAttribute("massageEntity", new MassageEntity());
        return "security/massageForAdmin";
    }

    @PostMapping("/massage/administration")
    public String resentMassageForAdmin(@ModelAttribute MassageEntity massageEntity){
        log.info("Init method resentMassageForAdmin in class ReadersController.");
        serviceMassage.save(massageEntity);
        return "redirect:/";
    }
}
