from django.db import models
from django.contrib.auth.models import User
# Create your models here.

class AppointmentQuery(models.QuerySet):

    def available(self):
        return self.filter(appointee = None)

    def booked(self):
        return self.filter(appointee__isnull = False)



class AppointmentManager(models.Manager):

    def get_queryset(self):
        return AppointmentQuery(self.model, using=self._db)

    def available(self):
        return self.get_queryset().available()

    def booked(self):
        return self.get_queryset().booked()

class Appointment(models.Model):

    tutor = models.ForeignKey(User, on_delete = models.CASCADE, null = True)
    appointee = models.CharField(max_length = 25, null = True, blank = True)
    time = models.DateTimeField()

    objects = AppointmentManager()

    def __str__(self):

        if self.appointee is None:
            return "Available appointment with {} on {}-{} at {}:00".format(
                self.tutor.username, self.time.month, self.time.day, self.time.hour)
        else:
            return "{}'s appointment with {} on {}-{} at {}:00".format(
                self.tutor.username, self.appointee, self.time.month, self.time.day, self.time.hour)