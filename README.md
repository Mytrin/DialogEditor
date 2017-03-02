## DialogEditor

Simple dialog library+editor for creation of RPG and adventure XML dialogs

Used libraries:
JDOM 2.0.6 (http://www.jdom.org)
GSON (https://github.com/google/gson)

# Example usage

example_project/tavern.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<root>
	<dialog id="innkeeper-start">
    <event source="innkeeper">
        		<text>Greetings traveler, what would you like?</text>
		</event>
    
    <responses>
        <response target="innkeeper-get-beer">
            <text>Give me some beer!</text>
		    </response>

        <response condition="GREATER_THAN" value1="$Player.Money" value2="5" target="innkeeper-get-mead">
            <text>I want mead!</text>
		    </response>
        
        <response target="exit()">
            <text>Milk!</text>
		    </response>
		</responses>
  </dialog>
  ...
</root>
```
Java:
```java
Dialogs dialogs = new Dialogs();
dialogs.loadFolder("example_project");

Dialog loadedDialog = dialogs.loadDialog("tavern:innkeeper-start");
Response[] responses = loadedDialog.getAvailableResponsesArray();

System.out.println(loadedDialog.getEvent())
for(Response response : responses){
   System.out.println(">>>"+response.getText());
}

dialogs.selectResponse(responses[2]);
System.out.println("<<<"+responses[2].getText());
```
Output:
```
Greetings traveler, what would you like?
>>>Give me some beer!
>>>I want mead!
>>>Milk!
<<<Milk!
```
