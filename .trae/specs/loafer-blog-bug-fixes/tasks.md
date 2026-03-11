# Loafer Blog Bug Fixes - Implementation Plan

## [ ] Task 1: Fix Announcement Creation Error
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - Modify the AnnouncementController to extract the userId from the request attribute set by JwtAuthenticationFilter
  - Update the AnnouncementServiceImpl to accept and set the authorId when creating an announcement
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-1.1: POST /api/announcements with valid JWT token should create announcement with correct author_id
  - `programmatic` TR-1.2: Announcement should be visible in the database with proper author_id
- **Notes**: The JwtAuthenticationFilter already sets the userId as a request attribute, so we need to extract it in the controller and pass it to the service

## [ ] Task 2: Fix Frontend Edit Buttons
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Implement edit forms for categories, announcements, posts, and tags in AdminView.vue
  - Update the edit functions in the Vue component to open the edit forms
  - Add save functionality for edited items
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgment` TR-2.1: Edit buttons should open forms with pre-filled data
  - `human-judgment` TR-2.2: Save button should update the item and close the form
- **Notes**: We'll use Vue's reactive state to manage edit forms and their visibility

## [ ] Task 3: Fix 403 Errors for Admin Endpoints
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Check and fix the role assignment in the JwtAuthenticationFilter
  - Ensure admin users are assigned the 'ADMIN' role
  - Test access to tag management, sensitive word management, and file size limit interfaces
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-3.1: GET /api/tags should return 200 for admin users
  - `programmatic` TR-3.2: GET /api/sensitive-words should return 200 for admin users
  - `programmatic` TR-3.3: GET /api/admin/file-limits should return 200 for admin users
- **Notes**: The JwtAuthenticationFilter currently sets a hardcoded 'ROLE_USER' authority, we need to fix this to properly assign roles

## [ ] Task 4: Implement Markdown File Storage Logic
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Modify the FileController's uploadMarkdownFile method to store the file on disk
  - Update the method to return both the file content and the file URL
  - Ensure the file is saved in the same directory as other uploads
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-4.1: POST /api/files/upload-markdown should store the file on disk
  - `programmatic` TR-4.2: Response should include both content and file URL
  - `programmatic` TR-4.3: File should be accessible via the returned URL
- **Notes**: Use the same file storage logic as the regular upload endpoint