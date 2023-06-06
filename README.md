# Spatial_Temporal_Resampling_and_Aliasing
 
For the full, detailed project description, please take a look at ASsignment1_Sprint_2023.pdf.
PART 1 - Spatial Resampling and Aliasing
PART 2 - Temporal Resampling and Aliasing

To invoke Mypart1.java:
please COMPILE using the command: javac Mypart1.java
please RUN using the command (choose desired parameters): java Mypart1 16 0.5 0

To invoke Mypart2.java:
please COMPILE using the command: javac Mypart2.java
please RUN using the command (choose desired parameters): java Mypart2 64 4.0 10.0

**Synopsis**: In this project, we gain a practical understanding of resampling and filtering in the spatial and temporal domain. Part 1 explores sampling and aliasing issues in the spatial domain, while part 2 explores sampling and aliasing issues in the temporal domain. In part 1, we display 2 images side by side: the left image will be the original image, and the right side is a resampled version of the original image. The resampled image is a scaled down version of the original image (utilized 3x3 average neighborhood filtering technique); this algorithm allows us to overcome/meet the Nyquist sampling criteria, resulting in minimal aliasing. In part 2, we display 2 videos side by side: the left video is a clockwise rotating radial pattern at a certain speed, and the right video is the same thing as the left except it will be given an fps (frames per second) rate of display. For the video on the right, it must be that the refresh rate (fps) is more than twice the rotational speed for them to look the same.