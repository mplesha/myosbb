/*
 * Project “OSBB” – a web-application which is a godsend for condominium head, managers and 
 * residents. It offers a very easy way to manage accounting and residents, events and 
 * organizational issues. It represents a simple design and great functionality that is needed 
 * for managing. 
 */
package com.softserve.osbb.controller;

import com.softserve.osbb.model.Option;
import com.softserve.osbb.model.User;
import com.softserve.osbb.service.OptionService;
import com.softserve.osbb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Roman on 28.07.2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/restful/option")
public class OptionController {

    private static final Logger logger = LoggerFactory.getLogger(OptionController.class);

    @Autowired
    private OptionService optionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{optionId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Resource<Option>> toScoreOption(@PathVariable("optionId") Integer optionId,
                                                          @PathVariable("userId") Integer userId) {
        logger.info("To score option: optionId=" + optionId + " userId=" + userId );
        Option option = optionService.getOption(optionId);
        User user = userService.findOne(userId);
        option.getUsers().add(user);
        user.getOptions().add(option);
        optionService.updateOption(option);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
