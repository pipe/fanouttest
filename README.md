
Generic tool for testing webRTC apps in development using google cloud functions

The pup-fun/twosession directory includes an example that uses puppeteer to test https://rendezvous.family/

To test your own app check this out and edit the files in twosession to refer to your (billable)
google cloud project and to the webRTC app under test.

(see also the Commcon.zyz talk I gave on this. - URL TBD )

The consumerAV directory is a (failed) attempt to run the |pipe| java-client code as a Google function.
It fails due to the Java network evironment in cloud functions not allowing iteration over the interfaces 
(and possibly more reasons). - Definitely a work in progress.
