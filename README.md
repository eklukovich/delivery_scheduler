# Objective
The primary objective is to build an Android application that displays a list of drivers from a set of JSON values. When the user taps a driver, the assigned shipment must be displayed where the driver's suitability score (SS) is maximized across all drivers and shipments. Each driver can only have one shipment and each shipment can only have one driver.

The suitability score is calculated as follows:
- If the shipment's street name length is even, the base SS is 1.5x the number of vowels in the driver’s name 
- If the shipment's street name length is odd, the base SS is 1x the number of consonants in the driver’s name
- If the length of the shipment's street name and length of the driver’s name share any common factors other than 1, the base SS is increased by 50% 

# Approach
The objective was broken down into three parts: the android app/architecture, the suitability score calculations, and assigning shipments to drivers. 

## Architecture
The Android application is designed as a single activity with fragments and uses the MVVM architecture pattern. A repository layer was added to abstract away where the data comes from and has an interface to define the data source, making it easy to swap out different data sources in the future. 

The app has the following packages:
- data: contains the data source and the hardcoded dataset
- repository: layer to access data from the ui
- scheduler: logic for scheduling drivers
- ui: fragment and viewmodel classes
- util: utility classes

Each layer has its own set of models to decouple 

## Suitability Score

## Driver Scheduling

