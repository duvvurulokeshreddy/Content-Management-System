package com.example.cms.service;


import org.springframework.http.ResponseEntity;

import com.example.cms.dao.BlogRequest;
import com.example.cms.dto.BlogResponse;
import com.example.cms.utility.ResponseStructure;

public interface BlogService {
	
	ResponseEntity<ResponseStructure<BlogResponse>> createBlog(BlogRequest blog,int userId);
	
	ResponseEntity<Boolean> checkBlogTitleAvailability(String blogTitle);
	
	ResponseEntity<ResponseStructure<BlogResponse>> findBlogById(int blogId);
	
	ResponseEntity<ResponseStructure<BlogResponse>> updateBlogData(BlogRequest blogReq,int blogId);

}
