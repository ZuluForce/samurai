Samurai is the tool I liked the most for analyzing thread dumps. However, the GUI was heavy and required a lot of memory when I have to correlate several thread dumps. 

Generating HTML was a neat option and once processed, the browser provided all the details the thick client provded and didn't consume as much resources. However,I had to run the GUI client every time to click the generate html button. Doing it ten times in a row was a drag.

This fork adds a way to generate HTML from the command line without having to open the thick client. 

Some of the tests still fail, so, use

`mvn clean install -DskipTests` 

to build the project.

Once built successfully, find the `gui-3.0-SNAPSHOT-jar-with-dependencies.jar` in the `gui/target` folder.

Run the jar file as follows to generate the samurai analysis in HTML

`java -jar gui-3.0-SNAPSHOT-jar-with-dependencies.jar -html -td <path_to_thread_dump_file> -o <output_folder> -p`

The `-p` flag is optional and prints the progress to the console when enabled.

Each genetated folder still copies all the image and css assets. An enhancement for multiple HTML dumps to use the same assets would be nice.
