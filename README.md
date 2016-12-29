# JAlgoArena Problems [![Build Status](https://travis-ci.org/spolnik/JAlgoArena-Problems.svg?branch=master)](https://travis-ci.org/spolnik/JAlgoArena-Problems)

JAlgoArena Problems is service dedicated for keeping problems definitions and their meta-data.

Demo: https://jalgoarena-ui.herokuapp.com/

- [Introduction](#introduction)
- [Components](#components)
- [Continuous Delivery](#continuous-delivery)
- [Infrastructure](#infrastructure)
- [Notes](#notes)

## Introduction

- JAlgoArena Problems allows user to query problems definitions and meta data required for Judge Agent to generate skeleton code in particular language. Additionally - it allows administrator to create new problems or edit existing ones.

![Component Diagram](https://github.com/spolnik/JAlgoArena/raw/master/design/component_diagram.png)

## Components

- [JAlgoArena UI](https://github.com/spolnik/JAlgoArena-UI)
- [JAlgoArena Judge](https://github.com/spolnik/JAlgoArena-Judge)
- [JAlgoArena Auth Server](https://github.com/spolnik/JAlgoArena-Auth)
- [JAlgoArena Eureka Server](https://github.com/spolnik/JAlgoArena-Eureka)
- [JAlgoArena API Gateway](https://github.com/spolnik/JAlgoArena-API)

## Continuous Delivery

- initially, developer push his changes to GitHub
- in next stage, GitHub notifies Travis CI about changes
- Travis CI runs whole continuous integration flow, running compilation, tests and generating reports
- coverage report is sent to Codecov
- application is deployed into Heroku machine

## Infrastructure

- Heroku (PaaS)
- Xodus (embedded highly scalable database) - http://jetbrains.github.io/xodus/
- Spring Boot, Spring Cloud (Eureka Client)
- TravisCI - https://travis-ci.org/spolnik/JAlgoArena-Problems

## Notes
- [Running locally](https://github.com/spolnik/jalgoarena/wiki)
- [Travis Builds](https://travis-ci.org/spolnik)

![Component Diagram](https://github.com/spolnik/JAlgoArena/raw/master/design/JAlgoArena_Logo.png)
