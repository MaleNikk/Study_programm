package com.example.web.news.controller;

import com.example.web.news.entity.CommentEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.service.ServiceComments;
import com.example.web.news.injections.service.ServiceReaders;
import com.example.web.news.security.injection.NewsAppSecurityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final ServiceComments<CommentEntity> serviceComments;

    private final ServiceReaders<ReaderEntity> serviceReaders;

    private final NewsAppSecurityService securityService;

    @GetMapping("/comments/showAllComments")
    public String showAll(Model model) {
        List<CommentEntity> allComments = getServiceComments().searchAll();
        model.addAttribute("commentsByNews", allComments);
        return "comments/showCommentsByNews";
    }

    @GetMapping("/comments/writeComment/{id}")
    public String addComment(@PathVariable Long id, Model model) {
        CommentEntity comment = new CommentEntity();
        comment.setId_news(id);
        model.addAttribute("commentEntity", comment);
        return "/comments/createComment";
    }

    @PostMapping("/comments/createComment")
    public String addCommentComplete(@ModelAttribute CommentEntity commentEntity, Model model) {
        Long check = commentEntity.getPin_reader();
        if (serviceComments.isPresentPinReader(check)) {
            if ((securityService.checkRoleAdmin(check)) || securityService.checkRoleModerator(check)) {
                AtomicLong atomicLong = new AtomicLong(getServiceComments().searchAll().size());
                long id;
                do {
                    id = atomicLong.getAndIncrement();
                } while (getServiceComments().numbersId().contains(id));
                getServiceComments().numbersId().add(id);
                commentEntity.setId(id);
                commentEntity.setDate_comment(LocalDateTime.now().toString());
                getServiceComments().save(commentEntity);
                if (commentsByNews(commentEntity.getId_news()).isEmpty()) {
                    return "redirect:/";
                } else {
                    model.addAttribute("commentsByNews", commentsByNews(commentEntity.getId_news()));
                    return "comments/showCommentsByNews";
                }
            } else { return "security/checkRole"; }
        } else {
            return "security/checkRegistration";
        }
    }

    @GetMapping("/comments/editCommentNews/{id}")
    public String editCommentsNews(@PathVariable Long id, Model model) {
        if (comment(id) != null) {
            model.addAttribute("commentEntity", comment(id));
            return "comments/editCommentNews";
        }
        return "redirect:/";
    }

    @PostMapping("/comments/editCommentNews")
    public String editCommentsNewsComplete(@ModelAttribute CommentEntity commentEntity, Model model) {
        Long checkComment = getServiceComments().searchById(commentEntity.getId()).getPin_reader();
        Long checkReader = commentEntity.getPin_reader();
        if (getServiceComments().isPresentPinReader(checkReader) && checkReader.equals(checkComment)
        && (getSecurityService().checkRoleModerator(checkReader) || getSecurityService().checkRoleAdmin(checkReader))) {
            getServiceComments().edit(commentEntity);
            model.addAttribute("commentsByNews", commentsByNews(commentEntity.getId_news()));
            return "comments/showCommentsByNews";
        } else {
            return "security/onlyAuthor";
        }
    }

    @GetMapping("/comments/editCommentAuthor/{id}")
    public String editCommentsAuthor(@PathVariable Long id, Model model) {
        if (comment(id) != null) {
            CommentEntity comment = comment(id);
            model.addAttribute("commentEntity", comment);
            return "comments/editCommentAuthor";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/comments/editCommentAuthor")
    public String editCommentsAuthorComplete(@ModelAttribute CommentEntity commentEntity, Model model) {
        CommentEntity checkPin = getServiceComments().searchById(commentEntity.getId());
        if (getServiceComments().isPresentPinReader(commentEntity.getPin_reader()) &&
                (commentEntity.getPin_reader()) == (checkPin.getPin_reader())) {
            getServiceComments().edit(commentEntity);
            model.addAttribute("commentsByAuthor", commentsByAuthor(commentEntity.getAuthor()));
            return "comments/showCommentsByAuthor";
        } else {
            return "security/onlyAuthor";
        }
    }

    @GetMapping("/showCommentsByAuthor/{author}")
    public String showCommentsByAuthor(@PathVariable String author, Model model) {
        model.addAttribute("commentsByAuthor", commentsByAuthor(author));
        return "comments/showCommentsByAuthor";
    }

    @GetMapping("/comments/showCommentsByNews/{id_news}")
    public String showCommentsByNews(@PathVariable Long id_news, Model model) {
        if (commentsByNews(id_news).isEmpty()) {
            return "redirect:/";
        } else {
            model.addAttribute("commentsByNews", commentsByNews(id_news));
            return "comments/showCommentsByNews";
        }
    }

    @GetMapping("/comments/deleteCommentNews/{id}")
    public String deleteCommentsNews(@PathVariable Long id, Model model) {
        long idRemove = comment(id).getId_news();
        //getServiceComments().removeById(id);
        if (commentsByNews(idRemove).isEmpty()) {
            return "redirect:/";
        } else {
            model.addAttribute("commentRemoved", comment(id));
            return "/comments/deleteComment";
        }
    }

    @GetMapping("/comments/deleteCommentAuthor/{id}")
    public String deleteCommentsAuthor(@PathVariable Long id, Model model) {
        CommentEntity comment = comment(id);
        //getServiceComments().removeById(id);
        List<CommentEntity> commentsByAuthor = commentsByAuthor(comment.getAuthor());
        if (commentsByAuthor.isEmpty()) {
            return "redirect:/";
        } else {
            model.addAttribute("commentRemoved", comment);
            return "/comments/deleteComment";
        }
    }

    @PostMapping("/comments/deleteComment")
    public String deleteCommentComplete(@ModelAttribute CommentEntity commentRemoved){
        if (commentRemoved != null ) {
            if (commentRemoved.getPin_reader() == comment(commentRemoved.getId()).getPin_reader()) {
               getServiceComments().removeById(commentRemoved.getId());
            } else {
                return "security/onlyAuthor";
            }
        }
        return "redirect:/";
    }

    private List<CommentEntity> commentsByNews(Long id_news) {
        return getServiceComments().commentsByNewsId(id_news);
    }

    private List<CommentEntity> commentsByAuthor(String author) {
        return getServiceComments().commentsByAuthorName(author);
    }

    private CommentEntity comment(Long id) {
        return getServiceComments().searchById(id);
    }
}
