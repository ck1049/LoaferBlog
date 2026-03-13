# LoaferBlog - Friend and Message Features Specification

## Overview
- **Summary**: Implement add friend functionality and message read/unread status features, including unread message indicators with counts.
- **Purpose**: Enhance user communication by enabling friend requests and providing clear indication of unread messages.
- **Target Users**: All registered users of LoaferBlog.

## Goals
- Implement complete add friend functionality
- Add read/unread status to messages
- Show red dot indicators with unread message counts in the top message button
- Show red dot indicators with unread message counts in contact list cards

## Non-Goals (Out of Scope)
- Modifying existing message content or delivery mechanism
- Adding new message types
- Implementing group chats
- Adding message notifications beyond the red dot indicators

## Background & Context
- The application already has a basic add friend view but needs complete functionality
- Messages currently don't have read/unread status tracking
- Users need clear indication of unread messages to improve user experience

## Functional Requirements
- **FR-1**: Add Friend Functionality
  - Allow users to search for other users by username
  - Allow users to send friend requests
  - Handle friend request responses (accept/decline)
  - Display friends list

- **FR-2**: Message Read/Unread Status
  - Track read status for each message
  - Mark messages as read when viewed
  - Display different styles for read and unread messages

- **FR-3**: Unread Message Indicators
  - Show red dot with count on top message button for total unread messages
  - Show red dot with count on each contact card for unread messages from that contact

## Non-Functional Requirements
- **NFR-1**: Performance
  - Unread message indicators should update in real-time
  - No significant performance impact from new features

- **NFR-2**: User Experience
  - Clear and intuitive indicators for unread messages
  - Responsive design for all device sizes

- **NFR-3**: Accessibility
  - Ensure indicators are visible to users with visual impairments
  - Maintain keyboard navigation support

## Constraints
- **Technical**: Must integrate with existing Vue 3 + TypeScript codebase
- **Dependencies**: Existing Pinia stores and axios for API calls

## Assumptions
- Backend API endpoints for friend management and message read status already exist
- User authentication is already implemented

## Acceptance Criteria

### AC-1: Add Friend Functionality
- **Given**: User is logged in
- **When**: User navigates to Add Friend page and searches for a user
- **Then**: User can send friend request to the found user
- **Verification**: `human-judgment`

### AC-2: Friend Request Handling
- **Given**: User receives a friend request
- **When**: User views friend requests
- **Then**: User can accept or decline friend requests
- **Verification**: `human-judgment`

### AC-3: Read/Unread Message Status
- **Given**: User receives a new message
- **When**: User opens the message conversation
- **Then**: Messages are marked as read
- **Verification**: `programmatic`

### AC-4: Unread Message Indicator on Message Button
- **Given**: User has unread messages
- **When**: User views the top navigation bar
- **Then**: Message button shows red dot with total unread count
- **Verification**: `human-judgment`

### AC-5: Unread Message Indicator on Contact Cards
- **Given**: User has unread messages from a contact
- **When**: User views the contact list
- **Then**: Contact card shows red dot with unread count for that contact
- **Verification**: `human-judgment`

## Open Questions
- [ ] What backend API endpoints are available for friend management?
- [ ] What backend API endpoints are available for message read status?
- [ ] Are there any existing UI components for red dot indicators?