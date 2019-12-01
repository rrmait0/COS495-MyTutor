from django.db import models
from django.contrib.auth.models import User
from PIL import Image
# Create your models here.

class Profile(models.Model):
    """Profile Model for users"""
    profile_picture = models.ImageField(upload_to='profile_pics', null=True)
    bio = models.TextField()
    rating = models.IntegerField(default=3)
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)

    def __str__(self):
        return "{} Profile".format(self.user.username)