# Loafer Blog Bug Fixes - Product Requirement Document

## Overview
- **Summary**: This document outlines the bugs identified in the Loafer Blog application and the required fixes to resolve them.
- **Purpose**: To address the following issues: frontend edit buttons not working, 403 errors for admin endpoints, missing Markdown file storage logic, and announcement creation error.
- **Target Users**: Developers and administrators of the Loafer Blog application.

## Goals
- Fix the frontend edit buttons for category, announcement, post, and tag management
- Resolve 403 errors for tag management, sensitive word management, and file size limit interfaces
- Implement proper storage logic for Markdown file uploads
- Fix the announcement creation error by properly setting the author_id field

## Non-Goals (Out of Scope)
- Adding new features or functionality
- Changing the overall architecture of the application
- Modifying the database schema

## Background & Context
- The application is a blog platform with admin functionality for managing content
- Authentication is handled via JWT tokens
- The backend is built with Spring Boot and MyBatis Plus
- The frontend is built with Vue 3 and Pinia

## Functional Requirements
- **FR-1**: Fix frontend edit buttons to properly open edit forms for categories, announcements, posts, and tags
- **FR-2**: Resolve 403 errors for admin-only endpoints by ensuring proper role assignment
- **FR-3**: Implement backend storage logic for Markdown file uploads
- **FR-4**: Fix announcement creation by properly setting the author_id field from the authenticated user

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
- Markdown files should be stored on the server

## Acceptance Criteria

### AC-1: Frontend Edit Buttons Work
- **Given**: User is logged in as admin
- **When**: User clicks edit button for category, announcement, post, or tag
- **Then**: Edit form opens with existing data pre-filled
- **Verification**: `human-judgment`

### AC-2: Admin Endpoints Accessible
- **Given**: User is logged in as admin
- **When**: User accesses tag management, sensitive word management, or file size limit interfaces
- **Then**: No 403 error is returned
- **Verification**: `programmatic`

### AC-3: Markdown File Upload Works
- **Given**: User is logged in as admin
- **When**: User uploads a Markdown file
- **Then**: File is stored on the server and content is extracted
- **Verification**: `programmatic`

### AC-4: Announcement Creation Works
- **Given**: User is logged in
- **When**: User creates a new announcement
- **Then**: Announcement is saved with proper author_id
- **Verification**: `programmatic`

## Open Questions
- [ ] What is the expected behavior for the edit forms? Should they be modal dialogs or inline edits?
- [ ] Where should Markdown files be stored on the server?
- [ ] How should admin roles be assigned to users?