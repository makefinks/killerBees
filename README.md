# About
This project was part of the module "Künstliche Intelligenz" (Artifical Intelligence) at the HFT Stuttgart. The goal of our group was to create a swarm simulation with logic that would enable a swarm to find targets in a configurable space.

# Map Editor
![image](https://user-images.githubusercontent.com/62705365/218259474-159d7f76-44bc-4e86-9e13-2454f0906c77.png)
Our main idea was to implement a map editor so that the user can freely draw obstacles and rooms in a space where one or mutiple swarms would later be trying to search for targets. The position of the Targets depicted as yellow circles and the starting position of the swarm depicted by red crosses can also be specified. There are multiple fields and settings that can be adjusted and selected:

**Number of Vehicles** 
> Specifies how many swarm objects will be in the simulation <br>

**Number of hits to destroy**

> Specifies the amount of hits (vehicle collides with target and disappears) it takes to destroy a target

**rand factor (0-1)**
> Specifies how random the movement of invidivual vehicles will be, with 1 being completly random

**enable sight**
> If enabled, vehicles can see targets from a distance if they are directly in front of them

**Random**
> If enabled, movements of swarm objects are completely random

**measure time**
> If enabled, the time for all vehicles to be destroyed will be measured (sleep field specifies the update intervall between movements)

# Simulation view
![image](https://user-images.githubusercontent.com/62705365/218259598-9c091146-d6bd-4235-9b2f-9bb338ac64e0.png)
Yellow circles -> Targets <br>
Gray lines-> sight of an individual

# Logic
Individuals of the swarm all have the following logic: 
1. Move towards the center of all vehicles in a specified radius
2. If another vehicle is to close, move away from it
3. Adjust the direction to the directon of all vehicles in a specified radius
4. If a target is within a small radius, move towards it
5. If a target collides with the sightline, move towards it
6. If a target is found, notify vehicles nearby so they can adjust their direction
