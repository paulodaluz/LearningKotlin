package com.example.bankaccount

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("accounts")
class AccountController(val repository: AccountRepository) {

    @PostMapping
    fun create(@RequestBody account: Account): ResponseEntity<Account> {
        val accountSave = repository.save(account)
        return ResponseEntity.ok(accountSave)
    }

    @GetMapping
    fun read(): ResponseEntity<MutableList<Account>> {
        val account = repository.findAll()
        return ResponseEntity.ok(account)
    }

    @PutMapping("{document}")
    fun update(@PathVariable document: String, @RequestBody account: Account): ResponseEntity<Account> {
        val accountDBOptional = repository.findByDocument(document)
        val accountToSave = accountDBOptional
            .orElseThrow { RuntimeException("Account document: $document not found!") }
            .copy(name = account.name, balance = account.balance)

        return ResponseEntity.ok(repository.save(accountToSave))
    }

    @DeleteMapping("{document}")
    fun delete(@PathVariable document: String) = repository
        .findByDocument(document)
        .ifPresent { repository.delete(it) }
}