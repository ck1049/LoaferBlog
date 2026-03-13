# LoaferBlog - Friend and Message Features Implementation Plan

## [ ] Task 1: Update Message Store with Read/Unread Status
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - Update Message interface to include read status field
  - Add methods to mark messages as read
  - Add state for tracking unread message counts
  - Update API calls to include read status
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-1.1: Messages are marked as read when opened
  - `programmatic` TR-1.2: Unread message counts are updated correctly
- **Notes**: Need to check backend API for read status endpoints

## [ ] Task 2: Implement Unread Message Indicators in Message Button
- **Priority**: P0
- **Depends On**: Task 1
- **Description**:
  - Add red dot indicator with count to message button in navigation bar
  - Update indicator when messages are read or new messages arrive
  - Ensure indicator is only shown when there are unread messages
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-2.1: Red dot appears with correct count when there are unread messages
  - `human-judgment` TR-2.2: Red dot disappears when all messages are read
- **Notes**: Need to locate the navigation bar component

## [ ] Task 3: Implement Unread Message Indicators in Contact List
- **Priority**: P0
- **Depends On**: Task 1
- **Description**:
  - Add red dot indicator with count to each contact card
  - Update indicator when messages from that contact are read
  - Ensure indicator is only shown when there are unread messages from that contact
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `human-judgment` TR-3.1: Red dot appears on contact cards with unread messages
  - `human-judgment` TR-3.2: Red dot count matches unread message count for each contact
- **Notes**: Modify MessageListView.vue

## [ ] Task 4: Complete Add Friend Functionality
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - Implement friend request sending functionality
  - Add friend request notification and handling
  - Implement friends list display
  - Add API calls for friend management
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `human-judgment` TR-4.1: User can send friend requests
  - `human-judgment` TR-4.2: User can accept/decline friend requests
  - `human-judgment` TR-4.3: Friends list displays correctly
- **Notes**: Need to check backend API for friend management endpoints

## [ ] Task 5: Update Message Detail View for Read/Unread Status
- **Priority**: P1
- **Depends On**: Task 1
- **Description**:
  - Mark messages as read when conversation is opened
  - Display visual distinction between read and unread messages
  - Update UI to show read status clearly
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-5.1: Messages are marked as read when conversation is opened
  - `human-judgment` TR-5.2: Read and unread messages have different visual styles
- **Notes**: Modify MessageDetailView.vue

## [ ] Task 6: Testing and Bug Fixes
- **Priority**: P2
- **Depends On**: All previous tasks
- **Description**:
  - Test all new features thoroughly
  - Fix any bugs or issues found
  - Ensure responsive design works on all devices
  - Verify accessibility requirements are met
- **Acceptance Criteria Addressed**: All ACs
- **Test Requirements**:
  - `human-judgment` TR-6.1: All features work as expected
  - `human-judgment` TR-6.2: UI is responsive and accessible
- **Notes**: Test across different device sizes and browsers