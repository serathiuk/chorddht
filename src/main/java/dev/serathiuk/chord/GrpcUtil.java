package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.Node;

public class GrpcUtil {

    private GrpcUtil() {}

    public static ChordNode fromNode(Node node) {
        return new GrpcChordNode(node.getHost(), node.getPort());
    }

    public static Node toNode(ChordNode chordNode) {
        if(chordNode == null) {
            return Node.getDefaultInstance();
        }

        return Node.newBuilder()
                .setId(chordNode.getId())
                .setHost(chordNode.getHost())
                .setPort(chordNode.getPort())
                .build();
    }

}