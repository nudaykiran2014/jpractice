package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 41: gRPC ANNOTATIONS                                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * gRPC is a high-performance RPC framework (faster than REST).
 * 
 * Requires: grpc-spring-boot-starter
 */
public class _41_GrpcAnnotations {
    public static void main(String[] args) {
        System.out.println("=== gRPC ANNOTATIONS ===\n");
        System.out.println("@GrpcService       → Expose gRPC service");
        System.out.println("@GrpcClient        → Inject gRPC client stub");
        System.out.println("@GrpcGlobalInterceptor → Global interceptor");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @GrpcService
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Expose a gRPC service endpoint
 * 
 * EXAMPLE:
 * ---------
 *     // 1. Define .proto file
 *     // service UserService {
 *     //   rpc GetUser (UserRequest) returns (UserResponse);
 *     // }
 *     
 *     // 2. Implement the service
 *     @GrpcService
 *     public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
 *         
 *         @Override
 *         public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
 *             UserResponse response = UserResponse.newBuilder()
 *                 .setId(request.getId())
 *                 .setName("John Doe")
 *                 .setEmail("john@example.com")
 *                 .build();
 *             
 *             responseObserver.onNext(response);
 *             responseObserver.onCompleted();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @GrpcClient
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Inject a gRPC client to call other services
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         @GrpcClient("user-service")
 *         private UserServiceGrpc.UserServiceBlockingStub userStub;
 *         
 *         public User getUser(String userId) {
 *             UserRequest request = UserRequest.newBuilder()
 *                 .setId(userId)
 *                 .build();
 *             
 *             UserResponse response = userStub.getUser(request);
 *             return new User(response.getName(), response.getEmail());
 *         }
 *     }
 *     
 *     # application.yml
 *     grpc:
 *       client:
 *         user-service:
 *           address: static://localhost:9090
 *           negotiationType: plaintext
 * 
 * gRPC vs REST:
 * --------------
 * | Feature    | gRPC           | REST            |
 * |------------|----------------|-----------------|
 * | Protocol   | HTTP/2         | HTTP/1.1        |
 * | Format     | Protobuf       | JSON            |
 * | Speed      | Faster         | Slower          |
 * | Streaming  | Built-in       | Limited         |
 * | Browser    | Limited        | Native          |
 */
