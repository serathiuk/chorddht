package dev.serathiuk.chord.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.0)",
    comments = "Source: chord.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChordGrpc {

  private ChordGrpc() {}

  public static final java.lang.String SERVICE_NAME = "Chord";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.NodeId,
      dev.serathiuk.chord.grpc.Node> getFindSuccessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findSuccessor",
      requestType = dev.serathiuk.chord.grpc.NodeId.class,
      responseType = dev.serathiuk.chord.grpc.Node.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.NodeId,
      dev.serathiuk.chord.grpc.Node> getFindSuccessorMethod() {
    io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.NodeId, dev.serathiuk.chord.grpc.Node> getFindSuccessorMethod;
    if ((getFindSuccessorMethod = ChordGrpc.getFindSuccessorMethod) == null) {
      synchronized (ChordGrpc.class) {
        if ((getFindSuccessorMethod = ChordGrpc.getFindSuccessorMethod) == null) {
          ChordGrpc.getFindSuccessorMethod = getFindSuccessorMethod =
              io.grpc.MethodDescriptor.<dev.serathiuk.chord.grpc.NodeId, dev.serathiuk.chord.grpc.Node>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findSuccessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.NodeId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.Node.getDefaultInstance()))
              .setSchemaDescriptor(new ChordMethodDescriptorSupplier("findSuccessor"))
              .build();
        }
      }
    }
    return getFindSuccessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Node,
      dev.serathiuk.chord.grpc.Empty> getNotifyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notify",
      requestType = dev.serathiuk.chord.grpc.Node.class,
      responseType = dev.serathiuk.chord.grpc.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Node,
      dev.serathiuk.chord.grpc.Empty> getNotifyMethod() {
    io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Node, dev.serathiuk.chord.grpc.Empty> getNotifyMethod;
    if ((getNotifyMethod = ChordGrpc.getNotifyMethod) == null) {
      synchronized (ChordGrpc.class) {
        if ((getNotifyMethod = ChordGrpc.getNotifyMethod) == null) {
          ChordGrpc.getNotifyMethod = getNotifyMethod =
              io.grpc.MethodDescriptor.<dev.serathiuk.chord.grpc.Node, dev.serathiuk.chord.grpc.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notify"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.Node.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChordMethodDescriptorSupplier("notify"))
              .build();
        }
      }
    }
    return getNotifyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Empty,
      dev.serathiuk.chord.grpc.NodeData> getGetNodeDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNodeData",
      requestType = dev.serathiuk.chord.grpc.Empty.class,
      responseType = dev.serathiuk.chord.grpc.NodeData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Empty,
      dev.serathiuk.chord.grpc.NodeData> getGetNodeDataMethod() {
    io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Empty, dev.serathiuk.chord.grpc.NodeData> getGetNodeDataMethod;
    if ((getGetNodeDataMethod = ChordGrpc.getGetNodeDataMethod) == null) {
      synchronized (ChordGrpc.class) {
        if ((getGetNodeDataMethod = ChordGrpc.getGetNodeDataMethod) == null) {
          ChordGrpc.getGetNodeDataMethod = getGetNodeDataMethod =
              io.grpc.MethodDescriptor.<dev.serathiuk.chord.grpc.Empty, dev.serathiuk.chord.grpc.NodeData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNodeData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.NodeData.getDefaultInstance()))
              .setSchemaDescriptor(new ChordMethodDescriptorSupplier("getNodeData"))
              .build();
        }
      }
    }
    return getGetNodeDataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Entry,
      dev.serathiuk.chord.grpc.PutResponse> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "put",
      requestType = dev.serathiuk.chord.grpc.Entry.class,
      responseType = dev.serathiuk.chord.grpc.PutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Entry,
      dev.serathiuk.chord.grpc.PutResponse> getPutMethod() {
    io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.Entry, dev.serathiuk.chord.grpc.PutResponse> getPutMethod;
    if ((getPutMethod = ChordGrpc.getPutMethod) == null) {
      synchronized (ChordGrpc.class) {
        if ((getPutMethod = ChordGrpc.getPutMethod) == null) {
          ChordGrpc.getPutMethod = getPutMethod =
              io.grpc.MethodDescriptor.<dev.serathiuk.chord.grpc.Entry, dev.serathiuk.chord.grpc.PutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.Entry.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.PutResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChordMethodDescriptorSupplier("put"))
              .build();
        }
      }
    }
    return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.GetRequest,
      dev.serathiuk.chord.grpc.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = dev.serathiuk.chord.grpc.GetRequest.class,
      responseType = dev.serathiuk.chord.grpc.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.GetRequest,
      dev.serathiuk.chord.grpc.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<dev.serathiuk.chord.grpc.GetRequest, dev.serathiuk.chord.grpc.GetResponse> getGetMethod;
    if ((getGetMethod = ChordGrpc.getGetMethod) == null) {
      synchronized (ChordGrpc.class) {
        if ((getGetMethod = ChordGrpc.getGetMethod) == null) {
          ChordGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<dev.serathiuk.chord.grpc.GetRequest, dev.serathiuk.chord.grpc.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.serathiuk.chord.grpc.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChordMethodDescriptorSupplier("get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChordStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChordStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChordStub>() {
        @java.lang.Override
        public ChordStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChordStub(channel, callOptions);
        }
      };
    return ChordStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChordBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChordBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChordBlockingStub>() {
        @java.lang.Override
        public ChordBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChordBlockingStub(channel, callOptions);
        }
      };
    return ChordBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChordFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChordFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChordFutureStub>() {
        @java.lang.Override
        public ChordFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChordFutureStub(channel, callOptions);
        }
      };
    return ChordFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void findSuccessor(dev.serathiuk.chord.grpc.NodeId request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Node> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindSuccessorMethod(), responseObserver);
    }

    /**
     */
    default void notify(dev.serathiuk.chord.grpc.Node request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNotifyMethod(), responseObserver);
    }

    /**
     */
    default void getNodeData(dev.serathiuk.chord.grpc.Empty request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.NodeData> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeDataMethod(), responseObserver);
    }

    /**
     */
    default void put(dev.serathiuk.chord.grpc.Entry request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.PutResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     */
    default void get(dev.serathiuk.chord.grpc.GetRequest request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Chord.
   */
  public static abstract class ChordImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ChordGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Chord.
   */
  public static final class ChordStub
      extends io.grpc.stub.AbstractAsyncStub<ChordStub> {
    private ChordStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChordStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChordStub(channel, callOptions);
    }

    /**
     */
    public void findSuccessor(dev.serathiuk.chord.grpc.NodeId request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Node> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindSuccessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notify(dev.serathiuk.chord.grpc.Node request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNotifyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeData(dev.serathiuk.chord.grpc.Empty request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.NodeData> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeDataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void put(dev.serathiuk.chord.grpc.Entry request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.PutResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void get(dev.serathiuk.chord.grpc.GetRequest request,
        io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Chord.
   */
  public static final class ChordBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ChordBlockingStub> {
    private ChordBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChordBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChordBlockingStub(channel, callOptions);
    }

    /**
     */
    public dev.serathiuk.chord.grpc.Node findSuccessor(dev.serathiuk.chord.grpc.NodeId request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindSuccessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public dev.serathiuk.chord.grpc.Empty notify(dev.serathiuk.chord.grpc.Node request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNotifyMethod(), getCallOptions(), request);
    }

    /**
     */
    public dev.serathiuk.chord.grpc.NodeData getNodeData(dev.serathiuk.chord.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeDataMethod(), getCallOptions(), request);
    }

    /**
     */
    public dev.serathiuk.chord.grpc.PutResponse put(dev.serathiuk.chord.grpc.Entry request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     */
    public dev.serathiuk.chord.grpc.GetResponse get(dev.serathiuk.chord.grpc.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Chord.
   */
  public static final class ChordFutureStub
      extends io.grpc.stub.AbstractFutureStub<ChordFutureStub> {
    private ChordFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChordFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChordFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<dev.serathiuk.chord.grpc.Node> findSuccessor(
        dev.serathiuk.chord.grpc.NodeId request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindSuccessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<dev.serathiuk.chord.grpc.Empty> notify(
        dev.serathiuk.chord.grpc.Node request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNotifyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<dev.serathiuk.chord.grpc.NodeData> getNodeData(
        dev.serathiuk.chord.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeDataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<dev.serathiuk.chord.grpc.PutResponse> put(
        dev.serathiuk.chord.grpc.Entry request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<dev.serathiuk.chord.grpc.GetResponse> get(
        dev.serathiuk.chord.grpc.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_SUCCESSOR = 0;
  private static final int METHODID_NOTIFY = 1;
  private static final int METHODID_GET_NODE_DATA = 2;
  private static final int METHODID_PUT = 3;
  private static final int METHODID_GET = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_SUCCESSOR:
          serviceImpl.findSuccessor((dev.serathiuk.chord.grpc.NodeId) request,
              (io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Node>) responseObserver);
          break;
        case METHODID_NOTIFY:
          serviceImpl.notify((dev.serathiuk.chord.grpc.Node) request,
              (io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.Empty>) responseObserver);
          break;
        case METHODID_GET_NODE_DATA:
          serviceImpl.getNodeData((dev.serathiuk.chord.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.NodeData>) responseObserver);
          break;
        case METHODID_PUT:
          serviceImpl.put((dev.serathiuk.chord.grpc.Entry) request,
              (io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.PutResponse>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((dev.serathiuk.chord.grpc.GetRequest) request,
              (io.grpc.stub.StreamObserver<dev.serathiuk.chord.grpc.GetResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getFindSuccessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              dev.serathiuk.chord.grpc.NodeId,
              dev.serathiuk.chord.grpc.Node>(
                service, METHODID_FIND_SUCCESSOR)))
        .addMethod(
          getNotifyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              dev.serathiuk.chord.grpc.Node,
              dev.serathiuk.chord.grpc.Empty>(
                service, METHODID_NOTIFY)))
        .addMethod(
          getGetNodeDataMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              dev.serathiuk.chord.grpc.Empty,
              dev.serathiuk.chord.grpc.NodeData>(
                service, METHODID_GET_NODE_DATA)))
        .addMethod(
          getPutMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              dev.serathiuk.chord.grpc.Entry,
              dev.serathiuk.chord.grpc.PutResponse>(
                service, METHODID_PUT)))
        .addMethod(
          getGetMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              dev.serathiuk.chord.grpc.GetRequest,
              dev.serathiuk.chord.grpc.GetResponse>(
                service, METHODID_GET)))
        .build();
  }

  private static abstract class ChordBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChordBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return dev.serathiuk.chord.grpc.ChordOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Chord");
    }
  }

  private static final class ChordFileDescriptorSupplier
      extends ChordBaseDescriptorSupplier {
    ChordFileDescriptorSupplier() {}
  }

  private static final class ChordMethodDescriptorSupplier
      extends ChordBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ChordMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChordGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChordFileDescriptorSupplier())
              .addMethod(getFindSuccessorMethod())
              .addMethod(getNotifyMethod())
              .addMethod(getGetNodeDataMethod())
              .addMethod(getPutMethod())
              .addMethod(getGetMethod())
              .build();
        }
      }
    }
    return result;
  }
}
