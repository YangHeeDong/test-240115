package com.example.test240115.domain.article.controller;

import com.example.test240115.domain.article.entity.Article;
import com.example.test240115.domain.article.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public String list(Model model){
        List<Article> articles = articleService.findByAll();
        model.addAttribute("articles",articles);
        return "/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(Article article){

        return "/article/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createArticle(@Valid Article article, BindingResult br,Principal pr){

        if(br.hasErrors()){
            return "/article/create";
        }

        articleService.createArticle(article, pr);

        return "redirect:/article/detail/"+article.getId();
    }

    @GetMapping("/detail/{id}")
    public String articleDetail(@PathVariable("id") Long articleId, Model model){

        Article article = articleService.findById(articleId);

        model.addAttribute("article",article);

        return "/article/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyArticleForm(@PathVariable("id") Long articleId,Principal principal,Model model){

        Article article = articleService.findById(articleId);

        if(!article.getMember().getLoginId().equals(principal.getName())){
            throw new RuntimeException("권한이 없습니다.");
        }

        model.addAttribute("article",article);

        return "/article/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyArticle(@Valid Article article, BindingResult br,Principal pr){

        articleService.modifyArticle(article,br, pr);

        return "redirect:/article/detail/"+article.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long articleId,Principal pr){

        articleService.deleteArticle(articleId,pr);

        return "redirect:/article/list";
    }

}
