# DiversLogbook
This application was developed by Lars Johan Nybø, Jaran Godejord and Linn-Hege Kristensen. The report is in this repository's Wiki. In the report you'll find the application structure and information about how we implemented the required functionality. There was also a need for a server side. This is also explained in the report.

# Installation
- This application uses firebase as backend. The project should contain a google-services.jason, so no installation should be required.

- Currently, only signin with google account is implemented, since most android users usually have a google account. 

# Bugs
Unfortunately, there are a few bugs in this application. 
Among other, and the most severe: the application can't save logs to the database, as it overwrites some user data.
The reason for this is unknown, and we did not have the time to fix it in time, due to writing the bachelor thesis.

## Main Idea
As a diver, you are required to plan and log all your dives. 
In addition, one person (for every 4th diver) should stand on the surface to keep an eye on the divers (surface-guard) and make sure 
they come back up at the planned time.

Make an application that has three "stages" or "modes": 
- **Plan**: This mode is used before the dive, and you should be able to plan max-depth, max-dive-time and the approximate 
			area to stay within. In addition, information like dive-type, weather, temperature, tank-size, amount of air in the tank before the dive
			should be recorded.
			This data should then be synchronized between the divers and the responsible surface-guard.
			This plan should be checked against prior dives and a dive-table to ensure that the dive is safe to perform.
- **Dive**: During the dive, the surface-guard should be able to manually start a countdown-timer (for each diver) when the dive begins.
			The length of the timer is decided in the plan. There should be a "stop"-button (for each diver) if the divers end the dive early, and an alarm if the timer reaches 0.
			If the timer reaches 0; start another timer (time from preferences, default 3 min.) 
			that indicates when a rescue team should be contacted.
- **Log**:  When all the divers are safe and sound on the surface, the dive-start-time and dive-end-time from the surface-guard should be 
			synchronized with the divers' logs, and the saturation should be calculated automatically and based on prior dives from the last 24h.
			The divers should then be able to log additional information: end-tank-pressure, current-strength, visibility, notes, safety-measures.
	
	
	
	
	
	
