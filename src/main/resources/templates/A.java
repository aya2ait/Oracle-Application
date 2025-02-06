
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Oracle Security Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        form {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 10px;
        }

        input, select, button {
            margin-top: 5px;
        }
    </style>
</head>
<body>
<h1>Oracle Security Management</h1>

<!-- TDE: Enable Column Encryption -->
<form id="enableTDEForm">
    <h2>Enable Column Encryption</h2>
    <label for="tableName">Table Name:</label>
    <input type="text" id="tableName" name="tableName" required>

    <label for="columnName">Column Name:</label>
    <input type="text" id="columnName" name="columnName" required>

    <label for="algorithm">Algorithm:</label>
    <select id="algorithm" name="algorithm">
        <option value="AES256">AES256</option>
        <option value="AES128">AES128</option>
    </select>

    <button type="button" onclick="enableTDE()">Enable TDE</button>
</form>

<!-- TDE: Disable Column Encryption -->
<form id="disableTDEForm">
    <h2>Disable Column Encryption</h2>
    <label for="disableTableName">Table Name:</label>
    <input type="text" id="disableTableName" name="tableName" required>

    <label for="disableColumnName">Column Name:</label>
    <input type="text" id="disableColumnName" name="columnName" required>

    <button type="button" onclick="disableTDE()">Disable TDE</button>
</form>

<!-- TDE: Get Configurations -->
<form id="getTDEConfigForm">
    <h2>Get TDE Configurations</h2>
    <button type="button" onclick="getTDEConfigurations()">Get Configurations</button>
</form>

<!-- VPD: Create Policy -->
<form id="createVPDPolicyForm">
    <h2>Create VPD Policy</h2>
    <label for="policyName">Policy Name:</label>
    <input type="text" id="policyName" name="policyName" required>

    <label for="policyDefinition">Policy Definition (JSON):</label>
    <textarea id="policyDefinition" name="policyDefinition" rows="4" cols="50"></textarea>

    <button type="button" onclick="createVPDPolicy()">Create Policy</button>
</form>

<!-- VPD: Delete Policy -->
<form id="deleteVPDPolicyForm">
    <h2>Delete VPD Policy</h2>
    <label for="deletePolicyName">Policy Name:</label>
    <input type="text" id="deletePolicyName" name="policyName" required>

    <button type="button" onclick="deleteVPDPolicy()">Delete Policy</button>
</form>

<!-- VPD: Get Policies -->
<form id="getVPDPoliciesForm">
    <h2>Get VPD Policies</h2>
    <button type="button" onclick="getVPDPolicies()">Get Policies</button>
</form>

<script>
    const apiBase = "/api/security";

    async function enableTDE() {
        const tableName = document.getElementById("tableName").value;
        const columnName = document.getElementById("columnName").value;
        const algorithm = document.getElementById("algorithm").value;

        const response = await fetch(`${apiBase}/tde/enable?tableName=${tableName}&columnName=${columnName}&algorithm=${algorithm}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });

        const responseText = await response.text();
        alert(`TDE Enable Response: ${responseText}`);
    }

    async function disableTDE() {
        const tableName = document.getElementById("disableTableName").value;
        const columnName = document.getElementById("disableColumnName").value;

        const response = await fetch(`${apiBase}/tde/disable?tableName=${tableName}&columnName=${columnName}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });

        const responseText = await response.text();
        alert(`TDE Disable Response: ${responseText}`);
    }

    async function getTDEConfigurations() {
        const response = await fetch(`${apiBase}/tde/configurations`);
        const data = await response.json();

        console.log("TDE Configurations:", data);
        alert(JSON.stringify(data, null, 2));
    }

    async function createVPDPolicy() {
        const policyName = document.getElementById("policyName").value;
        const policyDefinition = document.getElementById("policyDefinition").value;

        const response = await fetch(`${apiBase}/vpd/policies`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ policyName, policyDefinition })
        });

        const responseText = await response.text();
        alert(`VPD Policy Creation Response: ${responseText}`);
    }

    async function deleteVPDPolicy() {
        const policyName = document.getElementById("deletePolicyName").value;

        const response = await fetch(`${apiBase}/vpd/policies/${policyName}`, {
            method: "DELETE"
        });

        const responseText = await response.text();
        alert(`VPD Policy Deletion Response: ${responseText}`);
    }

    async function getVPDPolicies() {
        const response = await fetch(`${apiBase}/vpd/policies`);
        const data = await response.json();

        console.log("VPD Policies:", data);
        alert(JSON.stringify(data, null, 2));
    }
</script>
</body>
</html>
