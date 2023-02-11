# About
This project was part of the module "Künstliche Intelligenz" (Artifical Intelligence) at the HFT Stuttgart. The goal of our group was to create a swarm simulation with logic that would enable a swarm to find targets in a configurable space.

# Map Editor
Our main idea was to implement a map editor so that the user can freely draw obstacles and rooms in a space where one or mutiple swarms would later be trying to search for targets. The position of the Targets depicted as yellow circles and the starting position of the swarm can also be specified. There are multiple fields and settings that can be adjusted and selected:

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
