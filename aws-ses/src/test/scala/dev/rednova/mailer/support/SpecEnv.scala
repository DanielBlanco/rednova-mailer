package dev.rednova.mailer.support

import dev.rednova.mailer.*
import zio.aws.sesv2.*

type SpecEnv = SesV2 & Mailer
