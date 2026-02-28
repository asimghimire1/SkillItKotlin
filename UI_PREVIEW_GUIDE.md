## 🎬 SKILLIT APP - UI PREVIEW GUIDE

### Quick Preview Instructions

To preview the app in Android Studio Emulator without lag:

#### **Option 1: Demo Mode (Recommended) ⭐**
1. Open Android Studio and run the app on an emulator
2. On the login screen, click the **"🎬 Try Demo Mode"** button
3. This automatically fills in test credentials
4. Click **"Log in"** to enter the student dashboard
5. You'll see all UI screens with mock data loaded instantly

#### **Option 2: Manual Login**
Use these test credentials:
- **Email:** `student@test.com`
- **Password:** `Test123456`
- **Role:** Student (automatic on login)

---

### 📱 What You'll See

#### **Navigation (4-Tab Bottom Pill Bar)**
1. **🏠 HOME** - Dashboard with quick actions and trending courses
2. **🏢 EXPLORE** - Course discovery with search and filters (Esewa, Khalti payment methods visible)
3. **📚 LEARNING** - "My Content" & "My Sessions" tabs
4. **💰 BIDS** - Manage bids with counter-offer modal

#### **Top Header Features**
- ✅ **Wallet Balance** - Shows NPR 5,000.00 (mock balance)
- 🔔 **Notification Bell** - Functional icon
- ⋮ **Menu Dropdown** - Click to see:
  - 📝 Add Credits (opens full credit purchase screen)
  - 🚪 Logout (with confirmation)

#### **Key Screens & Features**

**1. HOME SCREEN**
- Grid navigation items (Explore, Sessions, Bids, My Learning)
- Quick action buttons
- Trending courses section
- Upcoming live sessions

**2. EXPLORE SCREEN**
- Search bar with real-time filtering
- Category pills: All Topics, Design, Tech, Marketing, Business, Photography
- Course cards with:
  - Thumbnail image
  - Title, instructor, ratings
  - "Enroll Now" button (free courses)
  - "Place Bid" button (premium courses, opens bid modal)
- Bid placement sheet with validation (60-100% range)

**3. MY LEARNING SCREEN**
- Two tabs: "My Content" | "My Sessions"
- **My Content Tab:**
  - Continue Watching cards with video thumbnail
  - Progress bar
  - Play/Skip/Next buttons
  - Recent course cards with progress info
- **My Sessions Tab:**
  - Upcoming session list
  - Date, time, instructor info

**4. BIDS SCREEN**
- Step-by-step bidding guide at top
- Active bids section showing:
  - Bid status badges (PENDING, ACCEPTED, COUNTERED, REJECTED)
  - Course thumbnail
  - Instructor name
  - Your bid amount (in red)
  - Original price (gray)
- **Click any bid card** → Slides up "Counter Offer Received" modal with:
  - Your bid vs Teacher's counter offer
  - Slider to adjust bid amount
  - "Accept Counter Offer" button
  - "Send New Counter" link

**5. ADD CREDITS SCREEN**
- Read-only current balance card
- Three credit packages with selector:
  - NPR 500 - Starter Pack
  - NPR 1,000 - Standard Pack + 50 Bonus ⭐ RECOMMENDED
  - NPR 5,000 - Premium Pack + 500 Bonus
- Payment methods (Esewa, Khalti, Bank)
- "Proceed to Payment" button

---

### 📊 Mock Data Loaded

**Student Stats:**
- Total Courses: 5
- Completed: 2
- Total Hours: 45.5
- Credits: NPR 5,000.00
- Level: Intermediate
- Badges: Quick Learner, Problem Solver

**Mock Content (2 courses):**
1. Android UI/UX Design Basics (NPR 299, 240 mins)
2. Jetpack Compose Advanced (NPR 399, 360 mins)

**Mock Sessions (2 live):**
1. Advanced Kotlin Coroutines (90 mins, NPR 499)
2. Firebase Real-time Database (120 mins, NPR 599)

**Mock Bids (2 active):**
1. Android UI Design - PENDING (Your bid: NPR 199 | Original: NPR 299)
2. Jetpack Compose - COUNTERED (Counter offer: NPR 349)

**Mock Teachers:**
- John Smith (Rating: 4.8/5, 250 reviews)
- Sarah Connor (Rating: 4.9/5, 310 reviews)

---

### ✅ Features & Functionality

**Working UI Elements:**
- ✅ All 4 tab navigation
- ✅ Menu dropdown (Add Credits, Logout)
- ✅ Add Credits full page with payment methods
- ✅ Bid placement with counter offer modal
- ✅ Search and filtering on Explore
- ✅ Tab switching on My Learning
- ✅ Status badge styling (text-only, no backgrounds)
- ✅ Mock data instant load (no Firebase lag)

**Design System:**
- Primary Red: #EA2A33
- Dark backgrounds: #1A1A1A, #2A2A2A, #F5F5F5
- Status colors: Green (Accept), Orange (Counter), Red (Reject), Blue (Pending)
- Font: Plus Jakarta Sans (Material3 default)

---

### 🚀 Testing Tips

1. **Fastest Preview:** Use Demo Mode button
2. **No Network Needed:** All data is hardcoded mock data
3. **Instant Loading:** No Firebase delays
4. **Responsive UI:** Try rotating the emulator to test landscape
5. **Test All Tabs:** Click through each navigation item

---

### 🐛 Known & Expected Behavior

- ✅ All UI renders correctly with mock data
- ⚠️ Logout will attempt Firebase (may show brief loading)
- ⚠️ Add Credits "Proceed to Payment" is UI-only (no actual payment)
- ⚠️ Search filters work on mock data only
- ⚠️ Bid submissions use mock callbacks

---

### 📝 File Locations

- **Login Screen:** `SkillitLoginActivity.kt` (Demo button added)
- **Student Dashboard:** `StudentDashboardActivity.kt` (Main navigation)
- **UI Screens:**
  - Home: `StudentHomeScreen.kt`
  - Explore: `ExploreScreen.kt`
  - My Learning: `StudentMyLearningScreen.kt`
  - Bids: `StudentBidsScreen.kt`
- **Credits:** `AddCreditsScreen.kt`
- **Components:** `ui/components/Components.kt`
- **View Model:** `viewmodel/StudentViewModel.kt` (Mock data)

---

### 💡 Pro Tips

1. **For Screenshots:** Use emulator without Android Studio's UI (lighter)
2. **For Performance:** Reduce emulator RAM to 2GB if laggy
3. **For Testing:** Use Pixel 4 or Pixel 5 emulator (optimized)
4. **Dark Mode:** Built into Material3 theme (toggle available)

---

**Last Updated:** Feb 28, 2026
**Status:** ✅ Ready for UI Preview
