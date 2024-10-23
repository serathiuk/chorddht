# Serathiuk's Chord DHT
A simple Java Chord DHT Implementation.

## Usage

To execute the program, run the following command:

```bash
./mvnw exec:java
```

After the program is running, you can use the following commands:

### 1 - Start Chord 

Starts the Chord DHT local server. The server is necessary to communicate with other nodes. All nodes 
are client and server in same time.

You will be asked to provide the following information:

- **Local host**: the localhost address. Defaults to `localhost`.
- **Local port**: the port where the server will be listening. Defaults to `8000`.
- **Remote host**: the remote host address of a valid Chord node. Leave it blank if you want to start a new Chord ring.
- **Remote port**: the remote port address of a valid Chord node. Leave it as `0` if you want to start a new Chord ring.

### 2 - Stop  Chord

Stop the local Chord server and leave the network.

### 3 - Put a Key

Add a key to the Chord DHT. You will be asked to provide the following information:

- **Key**: the key to be added.
- **Value**: the value to be associated with the key.

### 4 - Get a Key

Get a key from the Chord DHT. You will be asked to provide the following information:

- **Key**: the key to be retrieved.

### 5 - Show node info

Show the node info. It will display the following information:

- **Node ID**: the node ID.
- **Successor ID**: the successor node ID.
- **Predecessor ID**: the predecessor node ID.

### 9 - Exit

Exit the program.


