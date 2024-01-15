package com.example.test240115.domain.article.service;

import com.example.test240115.domain.article.entity.Article;
import com.example.test240115.domain.article.repository.ArticleRepository;
import com.example.test240115.domain.member.entity.Member;
import com.example.test240115.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    @Transactional
    public void createArticle(Article article, Principal pr) {

        Member member = memberService.findByLoginId(pr.getName());

        article.setCreateDate(LocalDateTime.now());
        article.setModifyDate(LocalDateTime.now());
        article.setMember(member);

        articleRepository.save(article);
    }

    @Transactional
    public Article findById(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);

        if(article.isEmpty()){
            throw new RuntimeException("존재하지 않는 게시글입니다.");
        }

        return article.get();
    }

    public List<Article> findByAll() {
        return articleRepository.findAllByOrderByIdDesc();
    }

    public BindingResult modifyArticle(Article article, BindingResult br, Principal pr) {

        if(br.hasErrors()){
            return br;
        }

        Article findArticle = findById(article.getId());

        if(!findArticle.getMember().getLoginId().equals(pr.getName())){
            br.rejectValue("권한없음","권한이 없어요");
            return br;
        }

        findArticle.setModifyDate(LocalDateTime.now());
        findArticle.setTitle(article.getTitle());
        findArticle.setContent(article.getContent());

        articleRepository.save(findArticle);

        return br;
    }

    public void deleteArticle(Long articleId, Principal pr) {

        Article article = findById(articleId);

        Member member = memberService.findByLoginId(pr.getName());

        if(!article.getMember().getLoginId().equals(member.getLoginId())){
            return;
        }

        articleRepository.delete(article);

    }
}
