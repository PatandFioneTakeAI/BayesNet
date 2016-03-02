
Likelihood and rejection should have decreased 
Likelihood consistent 		
Rejection started converging after 600 at minimum 



Fiona Heaney & Pat Lebold
CS4341 - C16
Project #6
Option B Implementation - Created in Java


TO RUN: "java -jar BayesNets.jar network_option_b.txt <query.txt> <sample size>"

TO RECOMPILE: Open in IDE, export as runnable jar

DESIGN CHOICES: We created our net using a graph of nodes who kept track of  their parents. 

LIBRARIES USED: None

GRAPHS & TABLES: Please see analysis.xslx file included in submission 

CONVERGENCE:

Our particular samples exhibited abnormal behavior, where our rejection sampling had a lower variance and converged faster than our likelihood sampling. Although our likelihood sampling had a spike in the average for the 400/600 trials, it ultimately converged as well. Rejection sampling converged faster for us because it factors in more variables and therfore makes a more concise decision with less samples than likelihood sampling. 
With a larger sample size, rejections get more accurate over time. 

After talking to other classmates, we believe likelihood should have been converged faster because it is able to estimate unlikely queries earlier on. 







