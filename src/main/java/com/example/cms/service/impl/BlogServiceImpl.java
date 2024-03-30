package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.cms.dao.BlogRequest;
import com.example.cms.dto.BlogResponse;
import com.example.cms.dto.ContributionPanelResponse;
import com.example.cms.exception.BlogAlreadyExistsByTitleException;
import com.example.cms.exception.BlogNotFoundByIdException;
import com.example.cms.exception.IllegalAccessRequestException;
import com.example.cms.exception.InvalidPanelIdException;
import com.example.cms.exception.TopicsNotSpecifiedException;
import com.example.cms.exception.UserNotFoundByIdException;
import com.example.cms.model.Blog;
import com.example.cms.model.ContributionPanel;
import com.example.cms.model.User;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepo;
import com.example.cms.repository.UserRepository;
import com.example.cms.service.BlogService;
import com.example.cms.utility.ResponseStructure;
@Service
public class BlogServiceImpl implements BlogService {

	private ResponseStructure<BlogResponse> responseStructure;

	private BlogRepository blogRepo;

	private UserRepository userRepo;

	private ContributionPanelRepo contributionPanelRepo;

	private ResponseStructure<ContributionPanelResponse> responseStructures;

	public BlogServiceImpl(ResponseStructure<BlogResponse> responseStructure, BlogRepository blogRepo,
			UserRepository userRepo ,ContributionPanelRepo contributionPanelRepo,ResponseStructure<ContributionPanelResponse> responseStructures) {
		this.responseStructure = responseStructure;
		this.blogRepo = blogRepo;
		this.userRepo = userRepo;
		this.contributionPanelRepo =contributionPanelRepo;
		this.responseStructures =responseStructures;
	}


	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> createBlog(BlogRequest blog, int userId) {
		return userRepo.findById(userId).map(user -> {
			if(blogRepo.existsByTitle(blog.getTitle()))
				throw new BlogAlreadyExistsByTitleException("Title Already Present");

			if(blog.getTopics().length<1)
				throw new TopicsNotSpecifiedException("Failed Tp Create a Blog");

			Blog save=blogRepo.save(mappedtoBlog(blog,new Blog(),user));
			return ResponseEntity.ok(responseStructure.setMessage("Blog Created Sucess").setStatusCode(HttpStatus.OK.value()).setData(mappedToBlogResponse(save,new BlogResponse())));

		}).orElseThrow(()-> new UserNotFoundByIdException("User Id Not Found"));
	}


	private Blog mappedtoBlog(BlogRequest blogRequest,Blog blog,User user) {
		blog.setTitle(blogRequest.getTitle());
		blog.setTopices(blogRequest.getTopics());
		blog.setAbout(blogRequest.getAbout());
		blog.setUser(user);
		ContributionPanel panel = contributionPanelRepo.save(new ContributionPanel());
		blog.setContributionPanel(panel);
		return blog;
	}

	private BlogResponse mappedToBlogResponse(Blog blog,BlogResponse blogResponse) {
		blogResponse.setBlogId(blog.getBlogId());
		blogResponse.setTitle(blog.getTitle());
		blogResponse.setTopics(blog.getTopices());
		blogResponse.setAbout(blog.getAbout());
		return blogResponse;
	}

	@Override
	public ResponseEntity<Boolean> checkBlogTitleAvailability(String blogTitle) {
		return new ResponseEntity<Boolean>(blogRepo.existsByTitle(blogTitle),HttpStatus.FOUND);
	}


	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> findBlogById(int blogId) {
		return blogRepo.findById(blogId).map(blog -> {
			return ResponseEntity.ok(responseStructure.setMessage("Blog Found Success").setStatusCode(HttpStatus.OK.value()).
					setData(mappedToBlogResponse(blog, new BlogResponse())));

		}).orElseThrow(()-> new BlogNotFoundByIdException("Invalid Blog Id"));
	}


	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> updateBlogData(BlogRequest blogReq, int blogId) {
		return blogRepo.findById(blogId).map(blog -> {
			blog.setTitle(blogReq.getTitle());
			blog.setTopices(blog.getTopices());
			blog.setAbout(blogReq.getAbout());

			Blog save=blogRepo.save(blog);
			return ResponseEntity.ok(responseStructure.setMessage("Update Sucessfully").setStatusCode(HttpStatus.OK.value()).
					setData(mappedToBlogResponse(save, new BlogResponse())));
		}).orElseThrow(() -> new BlogNotFoundByIdException("Invalid Exception"));

	}


	@Override
	public ResponseEntity<ResponseStructure<ContributionPanelResponse>> addContributors(int userId, int panelId) {
		String email=SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepo.findByEmail(email).map(owner -> {
			return contributionPanelRepo.findById(panelId).map(panel -> {
				if(!blogRepo.existsByUserAndContributionPanel(owner, panel))
					throw new IllegalAccessRequestException("Failed To Add Contributor");
				return userRepo.findById(userId).map(contributor -> {
					panel.getList().add(contributor);
					contributionPanelRepo.save(panel);
					return ResponseEntity.ok(responseStructures.setMessage("Sucessfully Added Contributer ")
							.setStatusCode(HttpStatus.OK.value()).setData(new ContributionPanelResponse().setPanelId(panel.getPanelld())));

				}).orElseThrow(()-> new UserNotFoundByIdException("User Not Found"));
			}).orElseThrow(()-> new InvalidPanelIdException("Contributor PanelId Not Found "));
		}).get();

	}


	@Override
	public ResponseEntity<ResponseStructure<ContributionPanelResponse>> removeUserFromContributorPanel(int userId, int panelId) {
		
		String email=SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepo.findByEmail(email).map(owner -> {
			return contributionPanelRepo.findById(panelId).map(panel -> {
				if(!blogRepo.existsByUserAndContributionPanel(owner, panel))
					throw new IllegalAccessRequestException("Failed To Add Contributor");
				return userRepo.findById(userId).map(contributor -> {
					panel.getList().remove(contributor);
					contributionPanelRepo.save(panel);
					return ResponseEntity.ok(responseStructures.setMessage("Delete Added Contributer ")
							.setStatusCode(HttpStatus.OK.value()).setData(new ContributionPanelResponse().setPanelId(panel.getPanelld())));

				}).orElseThrow(()-> new UserNotFoundByIdException("User Not Found"));
			}).orElseThrow(()-> new InvalidPanelIdException("Contributor PanelId Not Found "));
		}).get();
	}



}
