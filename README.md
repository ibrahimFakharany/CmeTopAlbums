Album List
![Screenshot_1722558895](https://github.com/user-attachments/assets/39bf0c5e-ed16-49db-9c2b-49c02e09ad20)
![Screenshot_1722558892](https://github.com/user-attachments/assets/70c00051-0e9e-40c3-87e1-e2f5806e0b3e)

Album List is an Android application that lets users discover and enjoy top albums, with a focus on providing a seamless experience, even offline.

Features
Top Albums: Explore a curated list of the latest albums.
Offline Support: Automatically saves albums to your device, providing access even without an internet connection. When online, swipe to refresh and update the list.
Modern UI: Built using Jetpack Compose for a sleek and responsive user interface.
Efficient Data Handling:
Retrofit for fetching data from remote sources.
Realm for local data storage, capable of handling large data sets.
Coil for asynchronous image loading.
DataStore for saving and retrieving albumId keys and details.
Performance Optimizations: Leverages Kotlin Coroutines and Flow for efficient asynchronous operations, ensuring smooth performance.
Architecture
The app is designed with Clean Architecture and implements the Model-View-Intent (MVI) pattern. This approach ensures a clear separation of concerns, maintainability, and scalability, while providing a predictable and testable state management flow.
