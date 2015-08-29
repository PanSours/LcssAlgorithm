# Synopsis
This project enables to study the problem of finding similarity between trajectories (trajectory similarity problem) which is a known problem in the field of Data Mining. The algorithm called to deploy and solves the above problem is the LCSS(Longest Common Subsequence). Each orbit is described by a sequence geographical points (latidute, longitude). Each record dataset that we are given represents a path of a taxi that has taken place in the city of Beijing. Each line of the file contains the following separated by commas: taxi id, timestamp, latitude, longitude.

# Background
Each record dataset that we are given represents a path of a taxi that has taken place in the city of Beijing. One illustrative example of the format of the file is:

366, Mon Mar 03 00:05:59 EET 2014,39.90732,116.45353
366, Mon Mar 03 00:10:59 EET 2014,39.90729,116.45348
366, Mon Mar 03 00:15:59 EET 2014,39.90725,116.45334
366, Mon Mar 03 00:20:59 EET 2014,39.90722,116.4533
366, Mon Mar 03 00:25:59 EET 2014,39.90722,116.45327
366, Mon Mar 03 00:30:59 EET 2014,39.90725,116.4532
366, Mon Mar 03 00:35:59 EET 2014,39.9076,116.45309
366, Mon Mar 03 00:40:59 EET 2014,39.9077,116.453
366, Mon Mar 03 00:45:59 EET 2014,39.9076,116.45281
366, Mon Mar 03 00:50:59 EET 2014,39.90767,116.45271
366, Mon Mar 03 00:55:59 EET 2014,39.90771,116.45262

Each line of the file contains the following separated by commas:
taxi id, timestamp, latitude, longitude. The features we are usings are  the latitude (geographical
width of a point) and the longitude (longitude of a point).

In the project we have implemented the following:

1. Implementation of the algorithm LCSS and support of the implementation by using a window environment
corresponding framework (JavaFx).

2. We have modified our program so that having as input two trajectories S, Q with lengths
Ls, Lq and Ls >> Lq, to find the subset of S trajectory, which presents the greatest resemblance to the Q trajectory comparison. That is to
returning the S portion of length: Lq +  d (0 <d <Lq) having the largest percent similarity with Q.

3. Map Display (Google Maps) of the track comparison, in conjunction with
the most common orbit found through the LCSS.

# Installation
Platform : NetBeans IDE 7.4 or greater, Java version : 7 or greater.
All you have to do is open the project in Netbeans IDE as a JavaFx project.

# Running the project
You can run the application by running the MainWindow.java file. Then you have to select for which datasets you want to run the Lcss algorithm and hit the Procedure button.
If you want to see the trajectories on the map, you can hit the Google Maps button.
