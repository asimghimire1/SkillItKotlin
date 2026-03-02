# Quick Reference - Teacher Dashboard Components

## Navigation Flow

```
TeacherDashboardScreen (Main Container)
├── Dashboard → Floating Bottom Navigation
│   ├── + Session Button → ScheduleSessionScreenFunctional
│   ├── + Content Button → UploadContentScreenFunctional
│   ├── Learning → TeacherContentScreenFunctional
│   ├── Bids → TeacherBidsScreenFunctional
│   └── Earnings → TeacherEarningsScreenFunctional
├── VideoPlayerScreen (When video clicked)
└── SessionDetailsScreen (When session clicked)
```

---

## Screen Components

### 1. ScheduleSessionScreenFunctional

**Purpose**: Create and schedule new teaching sessions

**Key Features**:
- Session title input
- Category dropdown (Design, Technology, Business, Lifestyle)
- Difficulty level (Beginner, Intermediate, Advanced)
- Date picker (YYYY-MM-DD format)
- Time picker (HH:MM format)
- Meeting link input (Zoom, Google Meet, etc.)
- Free/Paid toggle with price input
- Draft saving capability
- Real-time form validation

**Usage**:
```kotlin
ScheduleSessionScreenFunctional(
    viewModel = viewModel,
    onBackClick = { currentScreen = "dashboard" },
    onSessionCreated = { 
        // Handle success - navigate back
        currentScreen = "dashboard"
    }
)
```

**Form Data Submitted To**:
- Firebase Realtime Database under `/sessions/{sessionId}`
- Triggers notification to enrolled students
- Updates teacher's session list

---

### 2. UploadContentScreenFunctional

**Purpose**: Upload video courses and educational content

**Key Features**:
- Video file picker (MP4, MOV, AVI)
- Thumbnail image preview and selector
- Video title input
- Category dropdown
- Full video description
- Free/Paid toggle with dynamic price field
- Upload progress indicator
- Terms & Conditions acceptance

**Usage**:
```kotlin
UploadContentScreenFunctional(
    viewModel = viewModel,
    videoLauncher = videoLauncher,  // From rememberLauncherForActivityResult
    thumbnailLauncher = thumbnailLauncher,
    onBackClick = { currentScreen = "dashboard" },
    onVideoUploaded = { currentScreen = "dashboard" }
)
```

**Upload Process**:
1. User selects video file → stored in `videoUri`
2. Optional thumbnail → stored in `thumbnailUri`
3. Both uploaded to Firebase Storage
4. URLs stored in `/videos/{videoId}` in Database
5. Video appears in "My Courses" tab

---

### 3. TeacherContentScreenFunctional

**Purpose**: Manage uploaded videos and scheduled sessions

**Key Features**:
- Two tabs: "My Courses" | "Live Sessions"
- My Courses Tab:
  - Course cards with thumbnails
  - Student count display
  - Revenue showing
  - Edit/Manage buttons
  - Status badges (Published/In Review)
- Live Sessions Tab:
  - Timeline-style session cards
  - Status indicators
  - Start/Join buttons
  - Upcoming session highlights

**Usage**:
```kotlin
TeacherContentScreenFunctional(
    viewModel = viewModel,
    onBackClick = { currentScreen = "dashboard" },
    onVideoClick = { video ->
        selectedVideo = video  // Opens VideoPlayerScreen
    },
    onSessionClick = { session ->
        selectedSession = session  // Opens SessionDetailsScreen
    }
)
```

**Data Loaded From**:
- `/videos` filtered by teacherId
- `/sessions` filtered by teacherId
- Real-time updates as content is added/modified

---

### 4. TeacherBidsScreenFunctional

**Purpose**: Manage student bids and price negotiations

**Key Features**:
- Three tabs: Active (8) | Pending | Completed
- Bid card showing:
  - Student profile picture
  - Course title with priority badge
  - Student name
  - Original price vs student offer
  - Counter Offer button
  - Accept Bid button
- Counter Offer Modal:
  - Price slider ($40-$60 range)
  - Real-time price display
  - Fee breakdown (85/15 split)
  - Send/Cancel buttons

**Usage**:
```kotlin
TeacherBidsScreenFunctional(
    viewModel = viewModel,
    onBackClick = { currentScreen = "dashboard" }
)
```

**Bid Lifecycle**:
1. Student submits bid with offer price
2. Teacher sees bid in "Active" tab
3. Teacher sends counter-offer (price + timestamp)
4. Bid moves to "Pending" tab
5. Teacher/Student negotiates
6. Bid accepted → moves to "Completed"
7. Payment processed

**Data from**:
- `/bids` filtered by teacherId
- Updates in real-time

---

### 5. TeacherEarningsScreenFunctional

**Purpose**: View earnings, balance, and transaction history

**Key Features**:
- Large balance display card (Red theme)
  - Withdraw Now button
  - History button
- Income Breakdown:
  - Courses card (red indicator)
  - Live Sessions card (gray indicator)
  - Growth percentages
- Weekly Earnings Chart:
  - 5-day bar chart (MON-FRI)
  - Stacked bars showing earning trends
- Recent Payouts:
  - 3 latest transactions
  - Type-specific icons
  - Status badges
  - View All link for history

**Usage**:
```kotlin
TeacherEarningsScreenFunctional(
    viewModel = viewModel,
    onBackClick = { currentScreen = "dashboard" }
)
```

**Data Displayed From**:
- `/teacher_balances/{teacherId}` → Total balance
- `/transactions/{teacherId}` → Transaction history
- Real-time balance updates

**Transaction Types**:
- Withdrawal: Bank transfer (green icon)
- Course Sale: Video course revenue (blue icon)
- Session Sale: Live session revenue (red icon)

---

## Reusable Composables

### VideoPlayerScreen
YouTube-style video player with ExoPlayer integration

```kotlin
VideoPlayerScreen(
    video = video,
    onBackClick = { /* navigate back */ },
    onPlayClick = { /* enroll or edit */ },
    isTeacher = false  // true for edit, false for enroll
)
```

**Displays**:
- Full-screen video with controls
- Video metadata
- Teacher info
- Description
- Related content suggestions
- Enrollment/Edit button

---

### SessionDetailsScreen
Complete session information display

```kotlin
SessionDetailsScreen(
    session = session,
    onBackClick = { /* navigate back */ },
    onJoinClick = { /* join or start session */ },
    isTeacher = false  // true for start, false for join
)
```

**Displays**:
- Session title
- Date, time, difficulty
- Category badge
- Meeting link (copyable)
- Status
- Description
- Join/Start button

---

### CourseCard
Individual course display in list

```kotlin
CourseCard(
    video = video,
    onClick = { /* open player */ },
    modifier = Modifier
)
```

---

### SessionCard
Individual session display in list

```kotlin
SessionCard(
    session = session,
    onClick = { /* open details */ },
    modifier = Modifier
)
```

---

### BidCard
Individual bid display with action buttons

```kotlin
BidCard(
    bid = bid,
    onCounterOffer = { /* show modal */ },
    onAccept = { /* accept bid */ },
    modifier = Modifier
)
```

---

### CounterOfferBottomSheet
Price negotiation modal popup

```kotlin
if (showCounterSheet) {
    CounterOfferBottomSheet(
        initialPrice = 48f,
        onDismiss = { showCounterSheet = false },
        onSend = { newPrice ->
            viewModel.sendCounterOffer(bidId, newPrice, teacherId)
            showCounterSheet = false
        }
    )
}
```

---

## Helper Composables

### PricingToggleButton
Free/Paid toggle button

```kotlin
PricingToggleButton(
    isPaid = isPaid,
    onToggle = { isPaid = it },
    modifier = Modifier.width(100.dp)
)
```

### SelectDropdown
Category/Difficulty selector

```kotlin
SelectDropdown(
    selectedValue = category,
    options = listOf("Design", "Technology", "Business", "Lifestyle"),
    onSelect = { category = it }
)
```

### TabButton
Tab navigation button

```kotlin
TabButton(
    title = "My Courses",
    isActive = selectedTab == 0,
    onClick = { selectedTab = 0 }
)
```

### IncomeCard
Income breakdown display

```kotlin
IncomeCard(
    title = "COURSES",
    amount = 8240f,
    growth = 14,
    color = Color(0xFFEA2A33),
    modifier = Modifier.weight(1f)
)
```

### TransactionItem
Transaction history list item

```kotlin
TransactionItem(
    transaction = transaction,
    modifier = Modifier.padding(...)
)
```

---

## ViewModel Functions

### Video Operations
```kotlin
// Load teacher's videos
viewModel.loadTeacherVideos(teacherId: String)

// Upload new video
viewModel.uploadVideo(
    videoUri: Uri,
    thumbnailUri: Uri?,
    title: String,
    description: String,
    category: String,
    isPaid: Boolean,
    price: Float,
    teacherId: String,
    teacherName: String
)

// Load single video
viewModel.loadVideo(videoId: String)

// Delete video
viewModel.deleteVideo(videoId: String, teacherId: String)
```

### Session Operations
```kotlin
// Create new session
viewModel.createSession(
    title: String,
    category: String,
    difficulty: String,
    description: String,
    date: String,
    time: String,
    meetingLink: String,
    isPaid: Boolean,
    price: Float,
    teacherId: String,
    teacherName: String
)

// Load teacher's sessions
viewModel.loadTeacherSessions(teacherId: String)

// Delete session
viewModel.deleteSession(sessionId: String, teacherId: String)
```

### Bid Operations
```kotlin
// Load teacher's bids
viewModel.loadTeacherBids(teacherId: String)

// Send counter-offer
viewModel.sendCounterOffer(
    bidId: String,
    counterPrice: Float,
    teacherId: String
)

// Accept bid
viewModel.acceptBid(bidId: String, teacherId: String)
```

### Earnings Operations
```kotlin
// Load balance
viewModel.loadTotalBalance(teacherId: String)

// Load transaction history
viewModel.loadTeacherTransactions(teacherId: String)

// Add new transaction
repository.addTransaction(transaction: Transaction)
```

---

## State Management

### UiState Sealed Class
```kotlin
sealed class UiState {
    object Idle : UiState()                      // Initial state
    object Loading : UiState()                   // Loading data
    data class Success(val message: String) : UiState()  // Success message
    data class Error(val message: String) : UiState()    // Error message
}
```

### Collecting State in Compose
```kotlin
val uiState by viewModel.uiState.collectAsState()
val videos by viewModel.videoList.collectAsState()
val totalBalance by viewModel.totalBalance.collectAsState()

// Handle state changes
when (uiState) {
    is UiState.Loading -> { /* show spinner */ }
    is UiState.Success -> { /* show message */ }
    is UiState.Error -> { /* show error */ }
    UiState.Idle -> { /* normal state */ }
}
```

---

## Data Models

### Video
```kotlin
Video(
    id: String,
    title: String,
    description: String,
    videoUrl: String,         // Firebase URL
    thumbnailUrl: String,     // Firebase URL
    category: String,
    isPaid: Boolean,
    price: Float,
    teacherId: String,
    teacherName: String,
    studentCount: Int,
    status: String,           // "published", "draft"
    createdAt: Long
)
```

### Session
```kotlin
Session(
    id: String,
    title: String,
    category: String,
    difficulty: String,       // "Beginner", "Intermediate", "Advanced"
    description: String,
    date: String,            // "YYYY-MM-DD"
    time: String,            // "HH:MM"
    meetingLink: String,     // Zoom, Google Meet URL
    isPaid: Boolean,
    price: Float,
    teacherId: String,
    teacherName: String,
    status: String,          // "scheduled", "live", "completed"
    studentsRegistered: Int
)
```

### Bid
```kotlin
Bid(
    id: String,
    studentId: String,
    studentName: String,
    studentProfileUrl: String,
    courseTitle: String,
    originalPrice: Float,
    studentOffer: Float,
    teacherId: String,
    priority: String,        // "urgent", "standard"
    status: String,          // "active", "pending", "completed"
    counterOffer: Float,
    counterOfferBy: String   // "teacher", "student"
)
```

### Transaction
```kotlin
Transaction(
    id: String,
    type: String,           // "withdrawal", "course_sale", "session_sale"
    amount: Float,
    timestamp: Long,
    status: String,         // "completed", "processing", "failed"
    description: String,    // "Withdrawal to Bank", "Course Sale: ..."
    courseTitle: String
)
```

---

## File Upload Process

```
User selects file (URI)
    ↓
Upload to Firebase Storage
    ↓
Get download URL
    ↓
Save metadata to Realtime Database
    ↓
Update UI with confirmation
    ↓
Show in content list
```

### Storage Paths:
- **Videos**: `gs://bucket/videos/{timestamp}_{title}`
- **Thumbnails**: `gs://bucket/thumbnails/{timestamp}_{title}`
- **Profiles**: `gs://bucket/profiles/{userId}/{timestamp}`

---

## Color Palette

```kotlin
Primary Red      = Color(0xFFEA2A33)   // All action buttons
Success Green    = Color(0xFF10B981)   // Success states, growth
Secondary Gray   = Color(0xFF6B7280)   // Text, disabled states
Light Gray       = Color(0xFFF3F4F6)   // Backgrounds, borders
Dark Gray        = Color(0xFF4B5563)   // Dark text
Border Gray      = Color(0xFFE5E7EB)   // Dividers, borders
```

---

## Tips & Best Practices

1. **Always check user authentication before operations**
   ```kotlin
   val userId = FirebaseAuth.getInstance().currentUser?.uid
   if (userId != null) {
       // Proceed with operation
   }
   ```

2. **Handle loading states for better UX**
   ```kotlin
   Button(
       onClick = { /* action */ },
       enabled = uiState !is UiState.Loading
   ) {
       if (uiState is UiState.Loading) {
           CircularProgressIndicator()
       } else {
           Text("Upload")
       }
   }
   ```

3. **Use coroutineScope for async operations**
   ```kotlin
   val coroutineScope = rememberCoroutineScope()
   
   Button(onClick = {
       coroutineScope.launch {
           viewModel.uploadVideo(...)
       }
   })
   ```

4. **Validate forms before submission**
   ```kotlin
   if (videoTitle.isNotEmpty() && videoUri != null) {
       // Proceed with upload
   }
   ```

5. **Show appropriate error messages**
   ```kotlin
   when (uiState) {
       is UiState.Error -> {
           Text(
               (uiState as UiState.Error).message,
               color = Color.Red
           )
       }
       // ...
   }
   ```

---

## Testing Checklist

- [ ] Can upload video file
- [ ] Can select thumbnail
- [ ] Can create session with all fields
- [ ] Video plays with all controls
- [ ] Session details display correctly
- [ ] Can send counter-offer with slider
- [ ] Bid acceptance updates status
- [ ] Balance updates after bid acceptance
- [ ] Transactions appear in history
- [ ] All navigation flows work
- [ ] Loading states show during operations
- [ ] Error messages display on failure
- [ ] All buttons are clickable and functional

