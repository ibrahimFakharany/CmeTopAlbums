Album List
![Screenshot_1722558892](https://github.com/user-attachments/assets/70c00051-0e9e-40c3-87e1-e2f5806e0b3e) ![Screenshot_1722558895](https://github.com/user-attachments/assets/39bf0c5e-ed16-49db-9c2b-49c02e09ad20)

Album List is an Android application that allows users to discover and enjoy top albums with ease, offering offline support for seamless access.

Features
Top Albums
Browse and explore a curated list of the latest and greatest albums.

Offline Support
The app saves albums to your device, allowing access even without an internet connection. When online, swipe to refresh and update the list with the latest albums.

Modern UI
Designed with Jetpack Compose for a sleek and responsive user interface.

Efficient Data Handling

Retrofit for fetching data from remote sources.
Realm for local data storage, offering efficient handling of large data sets.
Coil for asynchronous image loading.
DataStore for saving and retrieving the albumId key and album details.
Performance Optimizations
Utilizes Kotlin Coroutines and Flow for efficient asynchronous operations, ensuring smooth performance.

Architecture
The app is designed with Clean Architecture and implements the Model-View-Intent (MVI) pattern. This approach ensures:

Separation of Concerns: Clear separation between different layers of the app (data, domain, and presentation).
Maintainability: A modular codebase that is easy to maintain and extend.
Scalability: The architecture can easily accommodate new features and changes.
Predictable State Management: MVI pattern provides a unidirectional data flow, making the app's state predictable and testable.
