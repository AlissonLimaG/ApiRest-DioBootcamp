package project.dio.projeto_pessoal_dio_bootcamp.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.DepositRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.WithdrawalRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.DepositResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.TransferResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.WithdrawalResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.requestExceptions.MissingAuthHeadingException;
import project.dio.projeto_pessoal_dio_bootcamp.infra.security.TokenService;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.services.AccountService;

@RestController
@Tag(name = "AccountController", description = "Controller to simulate the dynamics of using a user's bank account, such as: deposit, withdrawals and transfer between accounts.")
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Deposit money into the account", description = "Deposits an amount into a specific user's account and returns informations of deposit and the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Account limit exceeded"),
            @ApiResponse(responseCode = "400", description = "User not exists.")
    })
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseRecord> accountDeposit(@RequestBody @Valid DepositRequestRecord depositRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User user = accountService.depositAccount(username, depositRequest.value());
        return ResponseEntity.ok(new DepositResponseRecord(user));
    }



    @Operation(summary = "Withdraw money from the account", description = "Withdraw an amount into a specific user's account and returns informations of sake and the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Not balance in account"),
            @ApiResponse(responseCode = "400", description = "User not exists.")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseRecord> accountWithdrawal(@RequestBody @Valid WithdrawalRequestRecord withdrawalRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User user = accountService.withdrawalAccount(username, withdrawalRequest.value());
        return ResponseEntity.ok(new WithdrawalResponseRecord(user, withdrawalRequest));
    }



    @Operation(summary = "Transfer money", description = "Transfer money for user especified for username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful."),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Not balance in sender account."),
            @ApiResponse(responseCode = "422", description = "Not limit in recipient account."),
            @ApiResponse(responseCode = "400", description = "Sender not exists."),
            @ApiResponse(responseCode = "400", description = "Recipient not exists.")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseRecord> transfer(@RequestBody @Valid TransferRequestRecord transferRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User userSender = accountService.transferAccount(username,transferRequest);
        return ResponseEntity.ok(new TransferResponseRecord(userSender,transferRequest));
    }



    // Metodo para validar o cabeçalho da requisição, checar se o token existe e se o username existe.
    public String validationRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) throw new MissingAuthHeadingException("Missing authorization header.");

        String username = tokenService.validateToken(authHeader.replaceAll("Bearer ", ""));
        if(username == null) ResponseEntity.badRequest().build();

        return username;
    }

}
