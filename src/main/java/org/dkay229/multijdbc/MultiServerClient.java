package org.dkay229.multijdbc;

import com.dkay229.msql.common.MsqlErrorCode;
import com.dkay229.msql.common.MsqlException;
import com.dkay229.msql.domain.*;
import com.dkay229.msql.proto.DatabaseServiceGrpc;
import com.dkay229.msql.proto.Dbserver;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class MultiServerClient {
    private final DatabaseServiceGrpc.DatabaseServiceBlockingStub rpcStub;
    private final Dbserver.ConnectionOrBuilder rpcConnection;

    public MultiServerClient(String url, Properties props) {
        String[] hostPortDb = url.replace("jdbc:multisql://", "").split("[/:]");
        String host = hostPortDb[0];
        Integer port = Integer.parseInt(hostPortDb[1]);
        String dbName = hostPortDb[2];
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // Disable SSL/TLS for simplicity (use plaintext)
                .build();
        this.rpcStub = DatabaseServiceGrpc.newBlockingStub(channel);
        // rpc Login(LoginRequest) returns (Connection);
        Dbserver.LoginRequest loginRequest = Dbserver.LoginRequest.newBuilder()
                .setUserId(props.getProperty("user"))
                .setToken(props.getProperty("password"))
                .build();

        Dbserver.LoginResponse loginResponse = rpcStub.login(loginRequest);
        if (!loginResponse.getErrorMessage().isEmpty()) {
            throw new MsqlException(MsqlErrorCode.BAD_LOGIN_ATTEMPT, loginResponse.getErrorMessage());
        }
        this.rpcConnection = Dbserver.Connection.newBuilder()
                .setConnectionId(loginResponse.getConnectionId())
                .setConnectionKey(loginResponse.getConnectionKey())
                .build();
    }
    private DatabaseServiceGrpc.DatabaseServiceBlockingStub loggedInStub() {
        if (rpcConnection==null) {
            throw new RuntimeException("not logged in");
        }
        return rpcStub;
    }
    public ExecuteQueryResponse executeQuery(String sql) {

        Dbserver.ExecuteQueryResponse r=loggedInStub().executeQuery(Dbserver.ExecuteQueryRequest.newBuilder().setSql(sql).build());
        ArrayList<ColumnMetadata> columnMetadata = new ArrayList<>();
        for (Dbserver.ColumnMetadata c:r.getRowMetadata().getColumnsList()) {
            columnMetadata.add(new ColumnMetadata(c.getName(), ColumnMetadata.CoumnTypeEnum.valueOf(c.getType().name()),c.getSize()));
        }
        RowMetadata rowMetadata = new RowMetadata(columnMetadata);
        return new ExecuteQueryResponse(r.getStatusCode(),r.getErrorMessage(),rowMetadata);
    }
    public ResultRowsResponse fetchResultRows(int notMoreThan) {
        Dbserver.ResultRowsResponse dbRowsResp = loggedInStub().fetchResultRows(Dbserver.ResultRowsRequest.newBuilder()
                .setConnection(Dbserver.Connection.newBuilder()
                        .setConnectionKey(rpcConnection.getConnectionKey())
                        .setConnectionId(rpcConnection.getConnectionId())
                        .build())
                .setNotMoreThanRowCount(notMoreThan)
                .build());
        ArrayList<Row> rows = new ArrayList<>();
        for (Dbserver.Row r:dbRowsResp.getRowsList()) {
            List<String> row = new ArrayList<>(r.getValuesList());
            rows.add(new Row(row));
        }
        return new ResultRowsResponse(rows);
    }
    public void close() {

    }
}
