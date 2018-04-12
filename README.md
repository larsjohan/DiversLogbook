# DiversLogbook
Project in Mobile/Wearable Programming

- Code: diverapp
- Category: diving/logging/rescue
- Supervisor: Lars Johan Nybø (larsjny@stud.ntnu.no)
- Notes: Student

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
			

## Requirements
- [ ] The final divers-log for the dive should include the following information:
	- [ ] Dive location
	- [ ] Dive type (recreation, work, photography, search&rescue, etc.)
	- [ ] Planned depth
	- [ ] Planned dive time
	- [ ] Time since last dive (Blank/none if longer than 24h)
	- [ ] Dive start time
	- [ ] Dive end time
	- [ ] Actual dive time
	- [ ] Saturation group (one uppercase letter from the dive-table)
	- [ ] Security stop: time and depth
	- [ ] Tank size in liters (and max pressure: 232bar or 300bar)
	- [ ] Start tank-pressure
	- [ ] End tank-pressure
	- [ ] Total air usage
	- [ ] Dive gas (Air or Nitrox). If Nitrox is selected, let the user specify O2% (21 - 100) 
		  and let the saturation be calculated manually.
	- [ ] Time since last alcohol-intake
	- [ ] Weather
	- [ ] Current (none, light, medium, strong)
	- [ ] Temperature surface
	- [ ] Temperature water
	- [ ] Notes
	- [ ] Dive-buddy name and signature
- [ ] The data that is common for both divers/surface-guard should be synchronized between the devices
- [ ] The diver should have a profile with name, certifications etc.
- [ ] **Preferences**:
	- [ ] Language
	- [ ] Specify a dive table: PADI
	- [ ] Specify the rescue-buffer-time (Time from timer runs out to a rescue team should be called, default: 3 min.)

## Optional Features
- [ ] Get suggestions for location name based on GPS or known dive-sites in the area
- [ ] In the planning-stage, load a map (preferably a sea-map) over the area where the divers can plan a route.
- [ ] Let the user plan future dives.
- [ ] Add calculation of saturation based on a Trimix and/or Nitrox gas (Difficult)
- [ ] Create achievents using Android Play Games Center
- [ ] **Preferences**:
	- [ ] Measurement units: *imperial* or *metric*
	- [ ] Add NDF-dive table
	
	
	
	
	
	
