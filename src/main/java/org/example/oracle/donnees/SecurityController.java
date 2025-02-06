package org.example.oracle.donnees;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {
    private final OracleSecurityService securityService;
    private static final String DEFAULT_USER = "SYSTEM"; // Temporary solution

    // TDE Endpoints
    @PostMapping("/tde/enable")
    public ResponseEntity<TDEConfig> enableTDE(
            @RequestParam String tableName,
            @RequestParam String columnName,
            @RequestParam String algorithm) {
        return ResponseEntity.ok(securityService.enableColumnEncryption(
                tableName, columnName, algorithm, DEFAULT_USER));
    }

    @PostMapping("/tde/disable")
    public ResponseEntity<Void> disableTDE(
            @RequestParam String tableName,
            @RequestParam String columnName) {
        securityService.disableColumnEncryption(tableName, columnName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tde/configurations")
    public ResponseEntity<List<TDEConfig>> getTDEConfigurations() {
        return ResponseEntity.ok(securityService.getAllTDEConfigurations());
    }

    // VPD Endpoints
    @PostMapping("/vpd/policies")
    public ResponseEntity<VPDPolicy> createVPDPolicy(@RequestBody VPDPolicy policy) {
        return ResponseEntity.ok(securityService.createPolicy(policy, DEFAULT_USER));
    }

    @DeleteMapping("/vpd/policies/{policyName}")
    public ResponseEntity<Void> dropVPDPolicy(@PathVariable String policyName) {
        securityService.dropPolicy(policyName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vpd/policies")
    public ResponseEntity<List<VPDPolicy>> getVPDPolicies() {
        return ResponseEntity.ok(securityService.getAllPolicies());
    }


}