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

## HTTPS Certificates
Depending on the CA used by your Merknera server, and the version of Java you are using, you may need to import your server's HTTPS certificate into your keystore.

To do this, first find your $JAVA_HOME directory.  If this is not set on your system, you can print it from Java by running the following:

`System.out.println(System.getProperty("java.home"));`

Navigate to $JAVA_HOME/lib/security, where there should be a file called `cacerts`

To view the contents of your certificate store, run the following command:

`keytool -list -keystore cacerts`

If prompted for a password, the default value is `changeit`

Navigate to your Merknera server, and download the HTTPS certificate from your browser.

Add it to your keystore with the following command:

`sudo keytool -import -noprompt -trustcacerts -alias <alias> -file <path to certificate you just downloaded> -keystore cacerts -storepass <password to cacerts>`

You will be prompted for your sudo password; enter this.

Java should now be able to connect to Merknera via HTTPS!

## References
This bot uses the following wonderful resources

* [JSON-RPC 2.0](http://software.dzhuvinov.com/json-rpc-2.0-base.html)
* [JSON Smart](https://code.google.com/archive/p/json-smart/)
* [Apache Commons IOUtils](https://commons.apache.org/proper/commons-io/)
