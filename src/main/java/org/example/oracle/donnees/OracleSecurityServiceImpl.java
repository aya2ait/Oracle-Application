package org.example.oracle.donnees;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OracleSecurityServiceImpl implements OracleSecurityService {
    private final TDEConfigRepository tdeConfigRepository;
    private final VPDPolicyRepository vpdPolicyRepository;
   // private final AuditConfigRepository auditConfigRepository;
    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA_NAME = "C##ALI";

    // TDE Methods
    @Transactional
    public TDEConfig enableColumnEncryption(String tableName, String columnName, String algorithm, String username) {
        try {
            String checkWalletSql = "SELECT STATUS FROM V$ENCRYPTION_WALLET";
            List<String> walletStatuses = jdbcTemplate.queryForList(checkWalletSql, String.class);

            if (!walletStatuses.contains("OPEN")) {
                throw new RuntimeException("Encryption wallet is not open. Please contact database administrator.");
            }

            String sql = String.format(
                    "ALTER TABLE %s.%s MODIFY %s ENCRYPT USING '%s'",
                    SCHEMA_NAME, tableName, columnName, algorithm
            );
            jdbcTemplate.execute(sql);

            TDEConfig config = new TDEConfig();
            config.setTableName(tableName);
            config.setColumnName(columnName);
            config.setEncryptionAlgorithm(algorithm);
            config.setCreatedAt(LocalDateTime.now());
            config.setCreatedBy(username);
            config.setActive(true);

            return tdeConfigRepository.save(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enable TDE encryption", e);
        }
    }

    @Transactional
    public void disableColumnEncryption(String tableName, String columnName) {
        TDEConfig config = tdeConfigRepository.findByTableNameAndColumnName(tableName, columnName);
        if (config == null) {
            throw new RuntimeException("No TDE configuration found");
        }

        try {
            String sql = String.format(
                    "ALTER TABLE %s.%s MODIFY %s DECRYPT",
                    SCHEMA_NAME, tableName, columnName
            );
            jdbcTemplate.execute(sql);

            config.setActive(false);
            tdeConfigRepository.save(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to disable TDE encryption", e);
        }
    }

    @Override
    public List<TDEConfig> getAllTDEConfigurations() {
        return tdeConfigRepository.findAll();
    }

    // VPD Methods
    @Transactional
    public VPDPolicy createPolicy(VPDPolicy policy, String username) {
        if (vpdPolicyRepository.existsByPolicyName(policy.getPolicyName())) {
            dropPolicy(policy.getPolicyName());
        }

        try {
            String createFunctionSql = String.format(
                    "CREATE OR REPLACE FUNCTION %s.%s(schema_var IN VARCHAR2, table_var IN VARCHAR2) " +
                            "RETURN VARCHAR2 AS BEGIN %s END;",
                    SCHEMA_NAME, policy.getFunctionName(), policy.getPolicyFunction()
            );
            jdbcTemplate.execute(createFunctionSql);

            String addPolicySql = String.format(
                    "BEGIN DBMS_RLS.ADD_POLICY(" +
                            "object_schema => '%s', " +
                            "object_name => '%s', " +
                            "policy_name => '%s', " +
                            "function_schema => '%s', " +
                            "policy_function => '%s', " +
                            "statement_types => '%s'); END;",
                    SCHEMA_NAME, policy.getTableName(), policy.getPolicyName(),
                    SCHEMA_NAME, policy.getFunctionName(), policy.getStatementTypes()
            );
            jdbcTemplate.execute(addPolicySql);

            policy.setCreatedAt(LocalDateTime.now());
            policy.setCreatedBy(username);
            policy.setActive(true);

            return vpdPolicyRepository.save(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create VPD policy", e);
        }
    }

    @Transactional
    public void dropPolicy(String policyName) {
        VPDPolicy policy = vpdPolicyRepository.findByPolicyName(policyName);
        if (policy == null) {
            throw new RuntimeException("Policy not found");
        }

        try {
            String dropPolicySql = String.format(
                    "BEGIN DBMS_RLS.DROP_POLICY(" +
                            "object_schema => '%s', " +
                            "object_name => '%s', " +
                            "policy_name => '%s'); END;",
                    SCHEMA_NAME, policy.getTableName(), policy.getPolicyName()
            );
            jdbcTemplate.execute(dropPolicySql);

            String dropFunctionSql = String.format(
                    "DROP FUNCTION %s.%s",
                    SCHEMA_NAME, policy.getFunctionName()
            );
            jdbcTemplate.execute(dropFunctionSql);

            policy.setActive(false);
            vpdPolicyRepository.save(policy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to drop VPD policy", e);
        }
    }

    @Override
    public List<VPDPolicy> getAllPolicies() {
        return vpdPolicyRepository.findAll();
    }

}