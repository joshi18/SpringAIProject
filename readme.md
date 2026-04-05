# Spring AI Chatbot - Multi-Provider AI Integration

A comprehensive Spring Boot application demonstrating how to integrate multiple AI providers (OpenAI and Google Gemini) using Spring AI framework.

---

## Table of Contents

1. [Overview](#overview)
2. [What You Will Learn](#what-you-will-learn)
3. [Prerequisites](#prerequisites)
4. [Project Setup](#project-setup)
5. [Configuration](#configuration)
6. [API Modules](#api-modules)
    - [Module 1: Basic Chat](#module-1-basic-chat)
    - [Module 2: Streaming Responses](#module-2-streaming-responses)
    - [Module 3: Conversation Memory](#module-3-conversation-memory)
    - [Module 4: Code Review](#module-4-code-review)
7. [Project Structure](#project-structure)
8. [Key Concepts](#key-concepts)
9. [Troubleshooting](#troubleshooting)

---

## Overview

This project demonstrates building an AI-powered chatbot using Spring AI. The application supports:

- Multiple AI providers (OpenAI and Google Gemini) in a single application
- Runtime provider and model selection per request
- Streaming responses for real-time output
- Conversation memory to maintain context across messages
- Template-based prompt engineering for structured AI tasks

---

## What You Will Learn

- How to configure Spring AI with OpenAI
- How to connect to Google Gemini using OpenAI-compatible API
- Managing multiple ChatClient beans with qualifiers
- Implementing streaming responses using Server-Sent Events (SSE)
- Building conversation memory for stateful chat sessions
- Using prompt templates for structured AI tasks
- Token management and sliding window context handling

---

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- OpenAI API key (from https://platform.openai.com/api-keys)
- Gemini API key (from https://aistudio.google.com/apikey)
- Basic understanding of Spring Boot

---

## Project Setup

### Step 1: Download the project zip folder

### Step 2: Configure API Keys

Update `application.properties` with your API keys:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=your-openai-api-key
spring.ai.openai.chat.options.model=gpt-4o
spring.ai.openai.chat.options.temperature=1

# Gemini Configuration
gemini.api.key=your-gemini-api-key
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/openai
gemini.api.completions.path=/chat/completions
gemini.model.name=gemini-2.0-flash
```

### Step 3: Run the Application

```bash
mvn spring-boot:run
```

The application starts at `http://localhost:8081`

---

## Configuration

### application.properties

```properties
# Application Settings
spring.application.name=ai-chatbot
server.port=8081

# OpenAI Configuration (Auto-configured by Spring AI)
spring.ai.openai.api-key=your-api-key
spring.ai.openai.chat.options.model=gpt-5
spring.ai.openai.chat.options.temperature=1

# Gemini Configuration (Custom properties for OpenAI-compatible API)
gemini.api.key=your-api-key
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/openai
gemini.api.completions.path=/chat/completions
gemini.model.name=gemini-2.0-flash
```

### Property Details

| Property | Description |
|----------|-------------|
| `spring.ai.openai.api-key` | OpenAI API key - triggers auto-configuration |
| `spring.ai.openai.chat.options.model` | Default model (gpt-4o, gpt-4o-mini, gpt-3.5-turbo) |
| `spring.ai.openai.chat.options.temperature` | Creativity level: 0 = deterministic, 2 = very random |
| `gemini.api.key` | Google Gemini API key |
| `gemini.api.url` | Gemini's OpenAI-compatible endpoint |
| `gemini.model.name` | Gemini model (gemini-2.0-flash, gemini-1.5-pro) |

### Security Note

Never commit real API keys. Use environment variables:

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
gemini.api.key=${GEMINI_API_KEY}
```

---

## API Modules

---

### Module 1: Basic Chat

The simplest way to interact with AI - send a message, get a response.

**Endpoint:** `POST /api/chat`

**How It Works:**
1. User sends a message in the request body
2. Application selects the AI provider based on headers
3. AI processes the message and returns a response

**Headers:**

| Header | Required | Default | Description |
|--------|----------|---------|-------------|
| `AI-Provider` | No | `openai` | AI provider to use (`openai` or `gemini`) |
| `AI-Model` | No | Default from config | Override model (e.g., `gpt-4o`, `gemini-1.5-pro`) |
| `Content-Type` | Yes | - | Must be `text/plain` |

**Examples:**

1. Basic request with default settings (OpenAI):

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -d "What is Spring Boot?"
```

2. Using Gemini provider:

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -H "AI-Provider: gemini" \
  -d "Explain microservices architecture"
```

3. Using OpenAI with specific model override:

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -H "AI-Provider: openai" \
  -H "AI-Model: gpt-4o" \
  -d "Write a haiku about programming"
```

**Response:** Plain text response from the AI.

**Key Files:**
- `ChatController.java` - REST endpoint
- `ModelService.java` - Provider resolution
- `MultiModelConfig.java` - ChatClient bean configuration

---

### Module 2: Streaming Responses

Get AI responses in real-time as they are generated, instead of waiting for the complete response.

**Endpoint:** `GET /api/stream`

**How It Works:**
1. Client opens a connection with `Accept: text/event-stream`
2. AI generates tokens one by one
3. Server pushes each token to the client immediately
4. Client displays tokens as they arrive (typing effect)
5. Connection closes when response is complete

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `message` | Yes | - | The user's prompt |
| `provider` | No | `openai` | AI provider to use |
| `model` | No | Default from config | Override model |

**Examples:**

1. Basic streaming request:

```bash
curl -N "http://localhost:8081/api/stream?message=Tell%20me%20a%20story"
```

Note: The `-N` flag disables buffering to see real-time output.

2. Streaming with Gemini:

```bash
curl -N "http://localhost:8081/api/stream?message=Explain%20Java%20streams&provider=gemini"
```

3. Streaming with specific model:

```bash
curl -N "http://localhost:8081/api/stream?message=Write%20a%20poem&provider=openai&model=gpt-4o"
```

**JavaScript Client Example:**

```javascript
// Using EventSource API
const eventSource = new EventSource(
  '/api/stream?message=Hello'
);

eventSource.onmessage = (event) => {
  document.getElementById('output').textContent += event.data;
};

eventSource.onerror = () => {
  eventSource.close();
};
```

```javascript
// Using Fetch API with ReadableStream
async function streamChat(message) {
  const response = await fetch(`/api/stream?message=${encodeURIComponent(message)}`);
  const reader = response.body.getReader();
  const decoder = new TextDecoder();
  
  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    console.log(decoder.decode(value));
  }
}
```

**Streaming vs Blocking Comparison:**

| Approach | Behavior |
|----------|----------|
| Blocking (`.call()`) | User sends message, waits 5+ seconds, then sees complete response |
| Streaming (`.stream()`) | User sends message, sees tokens appearing in real-time (typing effect) |

**Key Files:**
- `StreamController.java` - SSE endpoint returning `Flux<String>`

---

### Module 3: Conversation Memory

Maintain context across multiple messages in a conversation. The AI remembers what was said earlier.

**Why Is This Needed?**

LLMs are stateless - each API call is independent with no memory. To create a chatbot that remembers context, we must:
1. Store all messages on our side
2. Send the full conversation history with each new message
3. Manage token limits to avoid exceeding context windows

**Endpoints:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/conversation/{conversationId}` | Send message with memory |
| GET | `/api/conversation/{conversationId}/info` | Get conversation metadata |
| GET | `/api/conversation/{conversationId}/history` | Get full message history |
| DELETE | `/api/conversation/{conversationId}` | Clear conversation |
| GET | `/api/conversation/list` | List all conversations |

**Headers (for POST):**

| Header | Required | Default | Description |
|--------|----------|---------|-------------|
| `AI-Provider` | No | `openai` | AI provider to use |
| `AI-Model` | No | Default from config | Override model |
| `Content-Type` | Yes | - | Must be `text/plain` |

**Examples:**

1. Start a new conversation:

```bash
curl -X POST http://localhost:8081/api/conversation/session-123 \
  -H "Content-Type: text/plain" \
  -d "Hi, my name is Aman"
```

Response:
```json
{
  "conversationId": "session-123",
  "response": "Hello Aman! Nice to meet you. How can I help you today?",
  "messageCount": 2,
  "totalTokens": 45
}
```

2. Continue the conversation (AI remembers your name):

```bash
curl -X POST http://localhost:8081/api/conversation/session-123 \
  -H "Content-Type: text/plain" \
  -d "What is my name?"
```

Response:
```json
{
  "conversationId": "session-123",
  "response": "Your name is Aman, as you mentioned at the start of our conversation!",
  "messageCount": 4,
  "totalTokens": 89
}
```

3. Get conversation info:

```bash
curl http://localhost:8081/api/conversation/session-123/info
```

Response:
```json
{
  "exists": true,
  "conversationId": "session-123",
  "messageCount": 4,
  "totalTokens": 89,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:32:15"
}
```

4. Get full conversation history:

```bash
curl http://localhost:8081/api/conversation/session-123/history
```

Response:
```json
{
  "conversationId": "session-123",
  "messages": [
    { "role": "user", "content": "Hi, my name is Aman" },
    { "role": "assistant", "content": "Hello Aman! Nice to meet you..." },
    { "role": "user", "content": "What is my name?" },
    { "role": "assistant", "content": "Your name is Aman..." }
  ]
}
```

5. Clear a conversation:

```bash
curl -X DELETE http://localhost:8081/api/conversation/session-123
```

6. List all active conversations:

```bash
curl http://localhost:8081/api/conversation/list
```

**Token Management:**

The application uses a sliding window approach to manage token limits:
- Default limit: 4000 tokens
- Keeps most recent messages that fit within the limit
- Older messages are dropped to make room for new ones

**Example:** Full history has 10,000 tokens, but only 4,000 can be sent.

| Message | Status |
|---------|--------|
| Msg1, Msg2 | Dropped (oldest) |
| Msg3, Msg4, Msg5 | Sent to AI (fits in 4,000 token budget) |

**Key Files:**
- `ConversationController.java` - REST endpoints
- `ConversationService.java` - Memory management
- `ConversationHistory.java` - Message storage and token tracking

---

### Module 4: Code Review

AI-powered code review using prompt templates. Submit code and receive detailed analysis.

**Endpoint:** `POST /api/code-review`

**How It Works:**
1. User submits code with optional language and business requirements
2. Application loads a prompt template from resources
3. Template placeholders are filled with user input
4. AI analyzes the code and returns a detailed review

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `provider` | No | `openai` | AI provider to use |
| `model` | No | `gpt-5` | Model to use |

**Request Body (JSON):**

| Field | Required | Default | Description |
|-------|----------|---------|-------------|
| `code` | Yes | - | Source code to review |
| `language` | No | `Java` | Programming language |
| `businessRequirements` | No | - | Context about what the code should do |

**Examples:**

1. Basic code review:

```bash
curl -X POST "http://localhost:8081/api/code-review" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "public void process(String s) { System.out.println(s.length()); }",
    "language": "Java"
  }'
```

Response:
```json
{
  "success": true,
  "language": "Java",
  "provider": "openai",
  "model": "gpt-5",
  "review": "## Code Review\n\n### Issues Found:\n1. **Null Pointer Risk**: The method does not check if the input string `s` is null...",
  "codeLength": 67,
  "hasBusinessRequirements": false
}
```

2. Code review with business requirements:

```bash
curl -X POST "http://localhost:8081/api/code-review?provider=openai&model=gpt-4o" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "public void process(String s) { System.out.println(s.length()); }",
    "language": "Java",
    "businessRequirements": "Should handle null strings gracefully and log errors"
  }'
```

3. Review Python code with Gemini:

```bash
curl -X POST "http://localhost:8081/api/code-review?provider=gemini" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "def calculate(x, y): return x / y",
    "language": "Python",
    "businessRequirements": "Must handle division by zero"
  }'
```

4. Health check:

```bash
curl http://localhost:8081/api/code-review/health
```

Response:
```json
{
  "status": "healthy",
  "service": "Code Review Service"
}
```

**Prompt Template:**

Create the file `src/main/resources/templates/code-review.txt` with this content:

~~~text
You are an expert code reviewer. Review the following {language} code.

CODE TO REVIEW:
{code}

BUSINESS REQUIREMENTS:
{businessRequirements}

Please provide a comprehensive review covering:
1. Code quality assessment
2. Potential bugs or issues
3. Performance considerations
4. Best practice recommendations
5. Security concerns (if any)
6. Suggestions for improvement

Format your response in a clear, structured manner.
~~~

**Key Files:**
- `CodeReviewController.java` - REST endpoint
- `CodeReviewService.java` - Prompt template handling
- `templates/code-review.txt` - Prompt template file

---

## Project Structure

**Java Source Files:** `src/main/java/com/hungrycoders/ai_chatbot/`

| Package | File | Description |
|---------|------|-------------|
| (root) | `AiChatbotApplication.java` | Main application entry point |
| config | `MultiModelConfig.java` | ChatClient beans for OpenAI and Gemini |
| controller | `ChatController.java` | Basic chat endpoint |
| controller | `StreamController.java` | Streaming chat endpoint (SSE) |
| controller | `ConversationController.java` | Conversation with memory endpoints |
| controller | `CodeReviewController.java` | Code review endpoint |
| service | `ModelService.java` | Provider resolution (factory pattern) |
| service | `ConversationService.java` | Conversation memory management |
| service | `CodeReviewService.java` | Prompt template handling |
| model | `ConversationHistory.java` | Conversation state and token management |

**Resource Files:** `src/main/resources/`

| Path | Description |
|------|-------------|
| `application.properties` | Configuration file |
| `templates/code-review.txt` | Prompt template for code review |

---

## Key Concepts

### 1. Multi-Provider Architecture

The application supports multiple AI providers through a factory pattern:

| User Request Header | ModelService Call | Returns |
|---------------------|-------------------|---------|
| `AI-Provider: openai` | `getChatClient("openai")` | `openaiChatClient` |
| `AI-Provider: gemini` | `getChatClient("gemini")` | `geminiChatClient` |

### 2. OpenAI Compatibility

Google Gemini provides an OpenAI-compatible API endpoint. This allows us to use Spring AI's OpenAI classes to connect to Gemini by changing the base URL:

```java
OpenAiApi geminiApi = OpenAiApi.builder()
    .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai")
    .apiKey(geminiKey)
    .build();
```

### 3. ChatClient Fluent API

Spring AI provides a fluent API for building prompts:

```java
// Blocking call
String response = chatClient.prompt()
    .user("Hello")
    .call()
    .content();

// Streaming call
Flux<String> stream = chatClient.prompt()
    .user("Hello")
    .stream()
    .content();

// With conversation history
ChatResponse response = chatClient.prompt()
    .messages(conversationHistory)
    .call()
    .chatResponse();
```

### 4. Token Management

LLMs have context window limits. The sliding window approach keeps recent messages within budget:

**Example:** Context Window Limit = 4000 tokens

| Message | Tokens | Included? |
|---------|--------|-----------|
| M1 | 500 | No (dropped) |
| M2 | 800 | No (dropped) |
| M3 | 1000 | Yes |
| M4 | 1200 | Yes |
| M5 | 600 | Yes |
| **Total Sent** | **2800** | Within limit |

### 5. Prompt Templates

External prompt templates improve maintainability:

| Step | Content |
|------|---------|
| Template | `Review this {language} code: {code}` |
| Variables | `language = "Java"`, `code = "..."` |
| Result | `Review this Java code: ...` |

---

## Troubleshooting

### Common Issues

**1. "Unknown provider" error**

Supported providers: `openai`, `gemini`. Check the `AI-Provider` header value.

**2. API Key errors**

Ensure your API keys are correctly set in `application.properties`:
- OpenAI: `spring.ai.openai.api-key=sk-...`
- Gemini: `gemini.api.key=AI...`

**3. Model not found**

Verify the model name is correct:
- OpenAI: `gpt-4o`, `gpt-4o-mini`, `gpt-3.5-turbo`
- Gemini: `gemini-2.0-flash`, `gemini-1.5-flash`, `gemini-1.5-pro`

**4. Streaming not working in browser**

Ensure you are using a client that supports SSE. In curl, use the `-N` flag to disable buffering.

**5. Conversation not remembering context**

Ensure you are using the same `conversationId` for all messages. Check if the conversation was cleared.

### Debugging Tips

Enable debug logging in `application.properties`:

```properties
logging.level.org.springframework.ai=DEBUG
logging.level.com.hungrycoders=DEBUG
```

---

## Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Google Gemini API](https://ai.google.dev/docs)
- [HungryCoders Course](https://hungrycoders.com)

---

## License

This project is part of the HungryCoders Spring AI Course.