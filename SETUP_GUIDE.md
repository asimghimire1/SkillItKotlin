# Setup & Integration Guide - Teacher Dashboard

## Prerequisites Installed ✅
- ExoPlayer (Media3) for video playback
- Firebase Storage for file uploads
- Firebase Realtime Database for data persistence
- Room (optional) for local caching
- Lifecycle ViewModel for state management
- Navigation Compose for screen routing

---

## Step 1: Firebase Configuration

### 1.1 Add google-services.json
1. Download `google-services.json` from Firebase Console
2. Place it in `app/` folder
3. Already configured in `build.gradle.kts` with `google.gms.google.services` plugin

### 1.2 Initialize Firebase in your Activity/Application

Add to your Application class or TeacherDashboardActivity:

```kotlin
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.ktx.storage

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase automatically initializes
        // Just access it when needed:
        val database = Firebase.database
        val storage = Firebase.storage
    }
}
```

### 1.3 Register Application Class in AndroidManifest.xml

```xml
<application
    android:name=".MyApplication"
    ...>
</application>
```

---

## Step 2: Update Teacher ID and Name

Replace all instances of these placeholder strings:

### In FunctionalScreens.kt:
```kotlin
// BEFORE:
viewModel.createSession(..., teacherId = "teacher_123", teacherName = "Teacher Name")

// AFTER:
viewModel.createSession(
    ...,
    teacherId = getCurrentTeacherId(), // From Firebase Auth
    teacherName = getCurrentTeacherName() // From Firestore Profile
)
```

### Helper function:
```kotlin
fun getCurrentTeacherId(): String {
    return FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
}

fun getCurrentTeacherName(): String {
    // Get from Firestore or SharedPreferences
    return "Teacher Name"
}
```

---

## Step 3: File Upload Implementation

### 3.1 Request Permissions in AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 3.2 Request Runtime Permissions (Android 6.0+)

```kotlin
val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        // Permission granted, proceed with upload
    }
}

// Request when needed:
permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
```

### 3.3 File Pick Integration

Already implemented with:
```kotlin
val videoLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    // Handle selected video file
}

// Launch picker:
videoLauncher.launch("video/*")
```

---

## Step 4: Database Security Rules

Set these in Firebase Console → Realtime Database → Rules:

```json
{
  "rules": {
    "videos": {
      ".read": true,
      ".write": "auth != null",
      ".validate": "newData.hasChildren(['title', 'videoUrl', 'teacherId'])"
    },
    "sessions": {
      ".read": true,
      ".write": "auth != null",
      ".validate": "newData.hasChildren(['title', 'date', 'teacherId'])"
    },
    "bids": {
      ".read": "auth != null",
      ".write": "root.child('bids').child($key).child('teacherId').val() == auth.uid || auth.uid == root.child('bids').child($key).child('studentId').val()",
      ".validate": "newData.hasChildren(['studentId', 'teacherId', 'studentOffer'])"
    },
    "transactions": {
      "$uid": {
        ".read": "auth.uid == $uid",
        ".write": "auth.uid == $uid"
      }
    },
    "teacher_balances": {
      "$uid": {
        ".read": "auth.uid == $uid || root.child('bids').child($key).child('teacherId').val() == auth.uid",
        ".write": "auth.uid == $uid"
      }
    },
    "teachers": {
      "$uid": {
        ".read": true,
        ".write": "auth.uid == $uid"
      }
    }
  }
}
```

---

## Step 5: Firebase Storage Rules

Set in Firebase Console → Storage → Rules:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Videos - only authenticated users can upload
    match /videos/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Thumbnails - only authenticated users
    match /thumbnails/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Profiles - user can only upload their own
    match /profiles/{userId}/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth.uid == userId;
    }
  }
}
```

---

## Step 6: Testing the Implementation

### 6.1 Create Sample Data in Firebase Console

```javascript
// Videos
{
  "videoId123": {
    "id": "videoId123",
    "title": "Advanced UI Design",
    "description": "Complete UI/UX course",
    "category": "Design",
    "isPaid": true,
    "price": 49.99,
    "teacherId": "teacher_uid_here",
    "teacherName": "John Doe",
    "studentCount": 125,
    "status": "published",
    "videoUrl": "https://storage.googleapis.com/...",
    "thumbnailUrl": "https://storage.googleapis.com/...",
    "createdAt": 1700000000000,
    "duration": 3600000
  }
}

// Sessions
{
  "sessionId123": {
    "id": "sessionId123",
    "title": "Live Coding Workshop",
    "category": "Technology",
    "difficulty": "Intermediate",
    "description": "Build an Android app live",
    "date": "2024-03-15",
    "time": "14:30",
    "meetingLink": "https://zoom.us/j/123456789",
    "isPaid": false,
    "price": 0,
    "teacherId": "teacher_uid_here",
    "status": "scheduled",
    "createdAt": 1700000000000,
    "studentsRegistered": 42
  }
}

// Bids
{
  "bidId123": {
    "id": "bidId123",
    "studentId": "student_uid",
    "studentName": "Jane Smith",
    "studentProfileUrl": "https://...",
    "courseTitle": "Advanced Calculus II",
    "originalPrice": 60,
    "studentOffer": 48,
    "teacherId": "teacher_uid_here",
    "priority": "urgent",
    "status": "active",
    "createdAt": 1700000000000,
    "counterOffer": 0,
    "counterOfferBy": ""
  }
}
```

### 6.2 Test Locally

1. Build and run on Android emulator/device
2. Navigate to each screen
3. Try uploading a small video (< 500MB)
4. Check Firebase Console for uploaded files
5. Verify data appears in Realtime Database

### 6.3 Check Firebase Console

Go to:
- **Storage**: See uploaded videos/thumbnails
- **Realtime Database**: See created videos/sessions/bids
- **Authentication**: See logged-in users

---

## Step 7: Integration with Authentication

### Add to TeacherDashboardActivity:

```kotlin
import com.google.firebase.auth.FirebaseAuth

class TeacherDashboardActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()
        
        // Check if user is logged in
        if (auth.currentUser == null) {
            startActivity(Intent(this, SkillitLoginActivity::class.java))
            finish()
        }
        
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { 
            TeacherDashboardScreen(
                teacherId = auth.currentUser?.uid ?: "",
                teacherName = auth.currentUser?.displayName ?: "Teacher"
            )
        }
    }
}
```

### Update TeacherDashboardScreen signature:

```kotlin
@Composable
fun TeacherDashboardScreen(
    teacherId: String = "",
    teacherName: String = ""
) {
    // ... rest of the code
    // Pass teacherId and teacherName to ViewModel functions
}
```

---

## Step 8: Error Handling

The implementation includes error handling through `UiState`:

```kotlin
// In any screen
val uiState by viewModel.uiState.collectAsState()

when (uiState) {
    is UiState.Loading -> {
        // Show loading indicator
        CircularProgressIndicator()
    }
    is UiState.Success -> {
        val message = (uiState as UiState.Success).message
        // Show success toast
        ShowToast(message)
    }
    is UiState.Error -> {
        val error = (uiState as UiState.Error).message
        // Show error dialog
        ShowErrorDialog(error)
    }
    UiState.Idle -> {
        // Normal state
    }
}
```

### Create Toast Helper:

```kotlin
@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
```

---

## Step 9: Features Ready for Testing

✅ **Video Upload**
- Select video from device
- Select thumbnail
- Set video title, description, category
- Set price (free or paid)
- Upload to Firebase Storage
- Save metadata to Database

✅ **Video Playback**
- YouTube-style player
- Play/pause/seek controls
- Video information display
- Teacher details
- Enrollment tracking

✅ **Session Management**
- Create sessions with all details
- Set date, time, meeting link
- Free or paid sessions
- Session status tracking
- Join/Start session

✅ **Bid Management**
- View all bids
- Filter by status (Active/Pending/Completed)
- Send counter-offers with price slider
- Accept bids
- Real-time fee calculation

✅ **Earnings Tracking**
- See total balance
- Income breakdown by source
- Weekly earnings chart
- Transaction history
- Download transaction details

---

## Step 10: Deployment Checklist

- [ ] Replace all "teacher_123" with actual user UID
- [ ] Configure google-services.json
- [ ] Set up Firebase project with Realtime DB and Storage
- [ ] Add security rules for database and storage
- [ ] Request necessary Android permissions
- [ ] Test file upload (use small files first)
- [ ] Test video playback
- [ ] Test all navigation flows
- [ ] Add proper error messages
- [ ] Implement user authentication
- [ ] Add push notifications
- [ ] Set up payment processing

---

## Common Issues & Solutions

### Issue: Video doesn't play
**Solution**: 
- Check video URL is accessible
- Verify ExoPlayer is properly initialized
- Check network connectivity
- Use valid video format (MP4, MOV, AVI)

### Issue: Upload fails
**Solution**:
- Check Firebase Storage bucket exists
- Verify storage rules allow uploads
- Check file size (max 500MB)
- Ensure network is stable

### Issue: Data not syncing
**Solution**:
- Verify Firebase connection
- Check database rules
- Ensure user is authenticated
- Check internet connectivity

### Issue: Permissions denied
**Solution**:
- Request runtime permissions
- Handle permission denial gracefully
- Provide clear error messages

---

## Next Steps

1. **Integrate with real authentication**
   - Connect Firebase Auth
   - Load user profile
   - Display actual user data

2. **Add payment processing**
   - Integrate Stripe/PayPal
   - Process withdrawals
   - Handle bid payments

3. **Implement notifications**
   - Firebase Cloud Messaging
   - Bid alerts
   - Session reminders

4. **Add analytics**
   - Track video views
   - Monitor engagement
   - Revenue reporting

5. **Optimize performance**
   - Implement caching
   - Optimize images
   - Background tasks

---

## Support & Documentation

- [ExoPlayer Documentation](https://developer.android.com/guide/topics/media/exoplayer)
- [Firebase Realtime Database](https://firebase.google.com/docs/database)
- [Firebase Storage](https://firebase.google.com/docs/storage)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

