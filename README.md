# Scrabble-Bot
A bot to play Scrabble on [Merknera](https://github.com/mleonard87/merknera)

To start, create a file named config.json at the root level of the project.  Copy the json below and populate it appropriately.

~~~~
{
    "merkneraURL": <URL of Merknera server>,
    "token": <Your Merknera token>,
    "hostPort": <Local port your Bot should listen on>,
    "endpointURL": <URL your Bot is listening on>,
    "botname": <Name of your Bot>,
    "botversion": "Version number of your Bot",
    "website": "<Your Bot's website>"
}
~~~~

## References
This bot uses the following wonderful resources

* [JSON-RPC 2.0](http://software.dzhuvinov.com/json-rpc-2.0-base.html)
* [JSON Smart](https://code.google.com/archive/p/json-smart/)
* [Apache Commons IOUtils](https://commons.apache.org/proper/commons-io/)
