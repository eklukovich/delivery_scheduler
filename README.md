
# Objective
The primary objective is to build an Android application that displays a list of drivers from a set of JSON values. When the user taps a driver, the assigned shipment must be displayed where the driver's suitability score (SS) is maximized across all drivers and shipments. Each driver can only have one shipment and each shipment can only have one driver.

The suitability score is calculated as follows:
- If the shipment's street name length is even, the base SS is 1.5x the number of vowels in the driver’s name 
- If the shipment's street name length is odd, the base SS is 1x the number of consonants in the driver’s name
- If the length of the shipment's street name and length of the driver’s name share any common factors other than 1, the base SS is increased by 50% 

# Approach
This section provides an overview of the assumptions and the approach taken for the design and implementation of this app. 

### Assumptions
- The number of drivers and shipments will always be equal
- White space is excluded from length calculations
- The shipment street name will exclude building number and any unit number:
	- ex. `123 Main St` -> `Main St`
	- ex. `4231 Applewood Ave Apt. 320` -> `Applewood Ave` 
- Addresses with unit numbers will only contain either `Apt` or `Suite`


### Architecture
The Android application is designed as a single activity with fragments and uses the MVVM architecture pattern. A repository layer was added to abstract away where the data comes from and has an interface to define the data source, making it easy to swap out different data sources in the future. Each layer has its own set of models to keep layers decoupled and uses coroutine Flows to transfer data between the layers. All code was designed keeping the single-responsibility principle in mind and focused around making the code testable.

The app has the following packages:
- **data**: Contains the data source and the hardcoded dataset
- **repository**: Layer to access data from the ui
- **scheduler**: Logic for scheduling drivers
- **ui**: Activity, Fragment and ViewModel classes
- **util**: Utility classes

### Suitability Score
The suitability score has been implemented to meet the criteria listed above and abstracted to its own class to promote reusability and testing. The [Euclidean Algorithm](https://en.wikipedia.org/wiki/Euclidean_algorithm) has been used to determine if there are any common factors between the shipment's street name and the driver's name.

### Driver Scheduling
Scheduling the drivers to shipments for maximum suitability can be categorized as an [assignment problem](https://en.wikipedia.org/wiki/Assignment_problem), where a number of agents need to complete a number of tasks in a way that minimizes the total cost. The Brute Force solution for these type of problems has a O(n!) time complexity and is not suitable for this application. Instead, there is a more optimized algorithm called the [Hungarian Algorithm](https://en.wikipedia.org/wiki/Hungarian_algorithm) to solve this same problem in a O(n<sup>3</sup>) time complexity. 

This application uses the Hungarian Algorithm to match the drivers to shipments, and implements it using the [JGraphT Java library](https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/alg/matching/KuhnMunkresMinimalWeightBipartitePerfectMatching.html). This library uses a bipartite graph for the implementation and requires that there are an equal number of drivers and shipments. The graph is constructed with two sets of vertices, the drivers and the shipments, and each driver-shipment vertex is connected with a weighted edge, where the weight is the suitability score. The Hungarian algorithm finds the minimized cost, but the objective is to find the maximized suitability score. The maximized problem can be converted over to a minimized problem by simply subtracting the maximum suitability score value from each suitability score. This transformation gives the algorithm the required input for the desired objecticve result.

# Installation
1. Install [Android Studio](https://developer.android.com/studio) if not already install
2. Clone this repository to a desired directory
3. Open Android Studio and open the `delivery_scheduler` project directory
4. Once Gradle is finished syncing, build and deploy the app to your device/emulator
	- Open the `Run` tab at the top menu and select `Run 'app'`
5.  The `Delivery Scheduler` app will automatically open once installed

# Usage
The app displays a list of drivers and will initially not have shipment assigned to them. Tap any of the drivers and the assigned shipment address for that driver will appear underneath the driver's name and a green check mark will appear.

<p align="left">
  <img src="https://github.com/eklukovich/delivery_scheduler/blob/master/screenshot.png" width="350">
</p>
