# References
  * The Espresso Tests use a TestRule adapted from [this SO post](https://stackoverflow.com/questions/37597080/reset-app-state-between-instrumentationtestcase-runs)
  * The GoogleFit API adapter, FitnessService, FitnessServiceFactor, the Testing FitnessService all adapted from [lab 4](https://github.com/UCSD-CSE-110-2020/lab4-fitness)
  * This website helped us learn to write unit tests with Robolectric: [Using Robolectric for Android unit testing on the JVM - Tutorial](https://www.vogella.com/tutorials/Robolectric/article.html)
  * This website helped us learn to build up RecyclerView: [Using the RecyclerView](https://guides.codepath.com/android/using-the-recyclerview) (accessed on February 4th, 2020)
  * This website helped us learn view animation for when the view enters and exits [Animating Android Activities and Views with Left and Right Slide Animations](https://kylewbanks.com/blog/left-and-right-slide-animations-on-android-activity-or-view) (accessed on February 10th, 2020)
  * We learned to get and display date from Java Calendar object from [this SO post](https://stackoverflow.com/questions/3574811/how-can-i-get-a-date-from-my-calendar) (accessed on February 3rd, 2020)
  
Note: If not specified, the access date for each of the above references is between February 2, 2020 and February 16, 2020.

# TODO
1. We NEED to write more tests that do the same thing a couple of times to make sure toggling doesn't mess it up
2. If possible, write tests that actually close and reopen the app to see what happens

# Design Patterns in Practice
1. **Adapter**
 - This pattern is the most used. Every GoogleFit or Firebase api is masked through an Adapter which implements an interface
 - examples include `GoogleFitAdapter implements FitnessService` which adapts Google's Fit API and `FirebaseAuthAdapter implements AuthService` which adapts Google's FirebaseAuth. 
 
2. **Builder**
 - Many of our data models have multiple optional member variables. The `interface Builder` and interfaces that extend this interface help facilitate this. 
 - Examples include `RouteBuilder implements IRouteBuilder` and `RouteEnvironmentBuilder implements IRouteEnvironmentBuilder` 
 
3. **Observer**
 - Though not used as extensively, this helps solve a lot of the issues that occur with Google APIs' async network calls. By implementing `Subject` and observers, classes that adapt these kinds of methods can notify our application when calls are complete.
 - An example of this is `interface AuthService extends Subject<AuthServiceObserver>`
