# Loafer Blog File and Post Fixes - Implementation Plan

## [ ] Task 1: Fix File Size Limit Update 403 Error
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - Find and implement the file size limit endpoint in the backend
  - Ensure the endpoint has proper admin authentication
  - Test the endpoint to ensure it works without 403 error
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: PUT /api/admin/file-limits with valid JWT token should return 200
  - `programmatic` TR-1.2: File size limits should be updated in the system
- **Notes**: Need to check if the endpoint exists and has proper security configuration

## [ ] Task 2: Implement Local Markdown File Parsing
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Modify the frontend to parse Markdown files locally using FileReader API
  - Remove the backend Markdown file upload endpoint call
  - Update the UI to handle local file parsing
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgment` TR-2.1: Selecting a Markdown file should load content into the editor
  - `human-judgment` TR-2.2: User should be able to edit content before saving
- **Notes**: Use FileReader API to read and parse Markdown files locally

## [ ] Task 3: Enhance Technical Post Editing Functionality
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Update the editPost function to properly load post content
  - Ensure categories and tags are pre-selected based on existing post data
  - Test the edit functionality to ensure all fields can be modified
- **Acceptance Criteria Addressed**: AC-3, AC-4
- **Test Requirements**:
  - `human-judgment` TR-3.1: Edit form should display post content in the editor
  - `human-judgment` TR-3.2: Existing categories and tags should be pre-selected
  - `human-judgment` TR-3.3: All fields should be editable and saveable
- **Notes**: Need to check how post data is structured and how categories/tags are associated

## [ ] Task 4: Improve Media File Upload to Display Access URL
- **Priority**: P2
- **Depends On**: None
- **Description**:
  - Update the file upload UI to display the returned access URL
  - Add a copy button for easy URL copying
  - Ensure the URL is clearly visible after upload
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `human-judgment` TR-4.1: Uploaded files should display their access URL
  - `human-judgment` TR-4.2: URL should be easy to copy
- **Notes**: Use the existing uploadedFiles array to display the URL