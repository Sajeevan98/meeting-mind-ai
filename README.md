# MeetingMind-AI

## AI-powered Meeting Intelligence Platform

MeetingMind-AI is an AI-powered meeting management platform that helps users organize meetings, upload meeting-related documents, and generate structured AI insights such as summaries, action items, decisions, risks, and next steps.

# Tech Stack

## Backend

* Java 21
* Spring Boot 4.1.x
* Spring Security
* Spring AI
* Spring Data JPA
* PostgreSQL
* Flyway
* Bean Validation
* Docker
* MapStruct

## Frontend

* React.js
* Vite
* Material UI (MUI)
* React Router
* TanStack React Query
* Axios
* React Hook Form
* Day.js
* Notistack

## AI Models

* Google Gemini
* OpenAI

### Future AI Providers (can add without change the service logic)

* Anthropic Claude
* DeepSeek
* Azure OpenAI

# Core Features

## Meeting Management

* Create meetings
* Update meeting title and description
* View meeting details
* Delete meetings
* Meeting status tracking
* System-generated creation and update timestamps
* Meeting attachment count
* Meeting analysis count

## Meeting Attachments

* Upload meeting attachments
* Retrieve meeting attachments
* Delete attachments
* Multipart file upload
* Attachment management from the Meeting Details page

## AI Meeting Analysis

* Generate AI-powered meeting analysis
* Select AI provider
* Select AI model
* View analysis history
* View structured analysis results
* Delete unwanted analyses
* Track analysis version
* Track provider and model
* Track processing time
* Track analysis status

## Structured AI Insights

AI analysis can extract:

* Meeting summary
* Action items
* Assignees
* Deadlines
* Decisions
* Risks
* Next steps

## Analysis Export

* Download analysis results as PDF
* Print analysis results

# Frontend Features

* Responsive Material UI layout
* Responsive sidebar and navigation
* Meeting list
* Reusable meeting cards
* Meeting details page
* Tab-based meeting navigation
* Overview tab
* Attachments tab
* AI Analysis tab
* Reusable forms and dialogs
* Loading states
* Empty states
* Error handling
* Success/error notifications
* URL-based meeting detail tabs

# Architecture & Development Practices

* RESTful API design
* DTO-based API boundaries
* MapStruct entity-to-DTO mapping
* Global exception handling
* Centralized API client using Axios
* React Query server-state management
* Reusable custom React hooks
* Separation of UI, hooks, services, and API layers
* SOLID principles
* Strategy Pattern for AI provider/model selection
* Factory Pattern for AI strategy selection
* Interface-based service layer
* System-generated auditing timestamps

# Project Goals

MeetingMind-AI is being developed as a production-oriented learning and portfolio project focused on:

* AI integration with Spring AI
* LLM provider abstraction
* Document processing
* Modern React development
* Server-state management
* REST API design
* Production-style error handling
* Reusable architecture
* Testing
* Docker-based development
* Scalable AI integration architecture

# Planned Features

The following features are planned for future development:

* Authentication and authorization
* User and organization management
* PDF/document viewer
* Asynchronous AI analysis
* Analysis progress tracking
* RAG-based meeting question answering
* Meeting search
* AI-generated email drafts
* Calendar integration
* Meeting reminders
* AI analysis comparison
* Multiple analysis versions
* Audit logging
* Cloud storage integration
* Production deployment

# Future AI Provider Support

The AI provider architecture is designed to support additional providers without tightly coupling business logic to a specific LLM provider.

Planned providers include:

* Google Gemini
* OpenAI
* Anthropic Claude
* DeepSeek
* Azure OpenAI

# Development Status

## Completed

* Meeting CRUD
* Meeting Details page
* Responsive frontend layout
* Meeting attachments
* Attachment upload
* Attachment deletion
* AI provider/model selection
* AI analysis generation
* Analysis history
* Structured AI results
* Analysis deletion
* Analysis PDF export
* React Query integration
* Error handling
* Notistack notifications
* Day.js date/time formatting
* CORS configuration
* Multipart file upload

## In Progress

* Advanced attachment/document experience
* AI processing improvements
* Production-level testing
* Authentication and authorization
* Deployment preparation

# License

This project is currently developed as a personal learning and portfolio project.
by Sajeevan.V
