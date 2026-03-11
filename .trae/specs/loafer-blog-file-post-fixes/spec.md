# Loafer Blog File and Post Fixes - Product Requirement Document

## Overview
- **Summary**: This document outlines the bugs identified in the Loafer Blog application related to file size limits, technical post creation, and technical post editing functionality.
- **Purpose**: To address the following issues: file size limit update failure with 403 error, incorrect Markdown file handling in technical posts, and incomplete technical post editing functionality.
- **Target Users**: Developers and administrators of the Loafer Blog application.

## Goals
- Fix file size limit update functionality to resolve 403 error
- Modify technical post creation to parse Markdown files locally in frontend
- Enhance technical post editing to include content editing and pre-select existing categories/tags

## Non-Goals (Out of Scope)
- Adding new features or functionality beyond the specified fixes
- Changing the overall architecture of the application
- Modifying the database schema

## Background & Context
- The application is a blog platform with admin functionality for managing content
- Authentication is handled via JWT tokens
- The backend is built with Spring Boot and MyBatis Plus
- The frontend is built with Vue 3 and Pinia

## Functional Requirements
- **FR-1**: Fix file size limit update endpoint to resolve 403 error
- **FR-2**: Modify Markdown file handling in technical post creation to parse files locally in frontend
- **FR-3**: Enhance technical post editing to include content editing and pre-select existing categories/tags
- **FR-4**: Improve media file upload functionality to display returned access URLs

## Non-Functional Requirements
- **NFR-1**: All fixes should maintain compatibility with existing codebase
- **NFR-2**: Fixes should not break existing functionality
- **NFR-3**: Changes should follow the existing code style and patterns

## Constraints
- **Technical**: Must work with existing Spring Boot, MyBatis Plus, Vue 3, and Pinia setup
- **Business**: Must be completed with minimal changes to existing code

## Assumptions
- The JWT token contains the user ID
- The authenticated user should have admin privileges for admin-only endpoints
- Markdown files can be parsed in the frontend using JavaScript

## Acceptance Criteria

### AC-1: File Size Limit Update Works
- **Given**: User is logged in as admin
- **When**: User updates file size limits
- **Then**: No 403 error is returned and limits are updated successfully
- **Verification**: `programmatic`

### AC-2: Markdown File Parsing Works Locally
- **Given**: User is creating a technical post
- **When**: User selects a local Markdown file
- **Then**: File is parsed locally and content is loaded into the editor
- **Verification**: `human-judgment`

### AC-3: Technical Post Editing Includes Content
- **Given**: User is editing a technical post
- **When**: User opens the edit form
- **Then**: Post content is displayed in the editor and can be modified
- **Verification**: `human-judgment`

### AC-4: Categories and Tags Pre-Selected in Edit Form
- **Given**: User is editing a technical post
- **When**: User opens the edit form
- **Then**: All existing categories and tags for the post are pre-selected
- **Verification**: `human-judgment`

### AC-5: Media File Upload Displays Access URL
- **Given**: User uploads a media file
- **When**: Upload is complete
- **Then**: The returned access URL is displayed for easy copying
- **Verification**: `human-judgment`

## Open Questions
- [ ] Where is the file size limit endpoint implemented?
- [ ] What Markdown parser should be used in the frontend?
- [ ] How are categories and tags stored for technical posts?