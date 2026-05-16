# NewBlackBox SDK → Loader APK Build Workflow

Ye workflow **3 goals** cover karta hai:
1. `NewBlackbox` se latest SDK (`Bcore-release.aar`) build karna.
2. Us AAR ko `Loader` source me use karna.
3. `Loader` APK (debug/release) build karna.

## 1) Prerequisites
- JDK 17 installed
- Android SDK + Build Tools installed
- NDK (project config me `29.0.13846066`)
- `ANDROID_HOME`/`ANDROID_SDK_ROOT` set ho
- Linux/macOS shell (bash)

## 2) One-command build (recommended)
Repo root se run karo:

```bash
bash scripts/build_loader_with_sdk.sh release
```

Debug APK ke liye:

```bash
bash scripts/build_loader_with_sdk.sh debug
```

## 3) Script kya karta hai
1. `NewBlackbox` me `:Bcore:assembleRelease` run karta hai.
2. Fresh `Bcore-release.aar` ko `Loader/app/libs/Bcore-release.aar` par copy karta hai.
3. `Loader` me requested variant build karta hai:
   - `debug` → `:app:assembleDebug`
   - `release` → `:app:assembleRelease`
4. End me output APK location print karta hai.

## 4) Manual workflow (without script)

```bash
# Step 1: Build SDK AAR
cd NewBlackbox
./gradlew :Bcore:assembleRelease

# Step 2: Copy SDK into Loader
cp -f Bcore/build/outputs/aar/Bcore-release.aar ../Loader/app/libs/Bcore-release.aar

# Step 3: Build Loader APK
cd ../Loader
./gradlew :app:assembleRelease
```

## 5) Output paths
- SDK AAR: `NewBlackbox/Bcore/build/outputs/aar/Bcore-release.aar`
- Loader debug APK: `Loader/app/build/outputs/apk/debug/app-debug.apk`
- Loader release APK: `Loader/app/build/outputs/apk/release/app-release.apk`

## 6) Recommended CI sequence
Agar CI/CD lagani ho to stage order aisa rakho:
1. Checkout
2. Java + Android SDK + NDK setup
3. Build `:Bcore:assembleRelease`
4. Copy AAR into `Loader/app/libs`
5. Build Loader APK
6. Upload APK artifact

---
Agar tum chaho to next step me main GitHub Actions workflow file bhi add kar dunga (`.github/workflows/android-build.yml`) jisse push pe auto build ho jaye.
