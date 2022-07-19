# No-Fly Zones
This application is part of a research project with the National Science Foundation. The full repository can be found [here](https://github.com/dullahgtt/REU-2022). It allows users to toggle the different zones and send that information to a Tello drone.

# Attribute-based Access Control (ABAC)

## Zones
There are three types of zones supported:
* Green zones: Areas that can be flown over at any time.
* Red zones: Areas that cannot be flown over at any time.
* Yellow zones: These zones are utilized when the drone cannot reach its destination without passing through a red zone. Red zones are converted into yellow zones and the drone can temporarily pass through.

## Policies
Currently the policies are stored in matrices that are each stored in SQLite tables. This is done so that the drone can have easy access to the zoning information. 

# Contributors
* [Abdullah Kamal](https://github.com/dullahgtt) developed the drone path finding and traversal algorithms.
* [Jeremy Vidaurri](https://github.com/Jeremy-Vidaurri) developed the zoning application.
* [Dr. Carlos Rubio-Medrano](https://carlosrubiomedrano.com/) for providing mentorship throughout the research experience. 