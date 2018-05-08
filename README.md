# Demo Bonita integrating Chain

This repository contains the materials to show how to integrate Bonita BPM with Chain.

The use case demonstrated in this demo is a car supply chain. 

This demo has been built using the Community Edition of Bonita BPM. 

A more complex demo that has been built with the Subscription edition of Bonita BPM can be viewed in this [recording] (https://www.bonitasoft.com/for-you-to-read/videos/accelerate-blockchain-technology-adoption-bonita-bpm-and-chain-core-0)

# Requirements

To run the demo on your machine you will need the following elements:
* [Bonita Studio 7.5.4 minimun](www.bonitasoft.com/downloads-v2)
* [Chain core](https://chain.com)

# Installation

* Start Chain core and connect to Test Network

* Clone this project on your local machine

* Open a terminal in this repository

* Build the project InitChain
```
cd InitChain
mvn package
```

* Run the project InitChain to init the Chain Test Network with the data for the demo
```
mvn exec:java -Dexec.mainClass="org.bonitasoft.chain.InitDemoSupplyChain"
```

* Start Bonita Studio
* Import the [process](/process/CarSupplyChain-1.0.bos)
* Open the Portal
* Import the [organization provided](/process/Organization_Data.xml)
* Associate a User profile to all users. Easy way to achieve that is to map the User profile to the role "member"
* Run the process

# Demo

On startup, the process will display the initialization form. Enter the following information:
* Username: johnDoe
* Car reference: bonitaWhiteCar

![Demo 1](/images/Demo-1.png?raw=true)

Then submit the task. 

Log in as storeManager to display the next task: Review order. As storeManager, grab the task and accept it. 

![Demo 2](/images/Demo-2.png?raw=true)

Refresh your task list to display the next tasK: Notify delay to customer. Grab the task and click on "Client accept"

![Demo 3](/images/Demo-3.png?raw=true)

Log in as productionManager to display the next task: Order a new car. As productionManager, grab the task, enter a date and submit it. 

![Demo 4](/images/Demo-4.png?raw=true)

This will issue a new asset to the blockchain. You can see the transaction in the Chain dashboard. 

![Chain 1](/images/Chain-1.png?raw=true)

Log in as storeManager to display the next task: Notify for availability. As storeManager, grab the task and submit it.

![Demo 5](/images/Demo-5.png?raw=true)

This will transfer the car asset from the store account to john doe account in the blockchain. You can see the transaction in the Chain dashboard. 

![Chain 2](/images/Chain-2.png?raw=true)


# More

If you interested in building your own application with Bonita BPM and Chain, you can find the source code of the connectors here: [Chain connectors for Bonita](https://github.com/Bonitasoft-Community/bonita-connector-chain)
