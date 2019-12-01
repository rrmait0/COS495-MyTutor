from rest_framework import serializers
from rest_framework.validators import UniqueTogetherValidator
from django.contrib.auth.models import User
from credentials.serializers import UserSerializer
from django.db.models import Q
from datetime import timedelta, datetime
from .models import Appointment
from pytz.reference import Eastern
import pytz

class AppointmentSerializer(serializers.ModelSerializer):

    #tutor = UserSerializer()
    tutor = serializers.PrimaryKeyRelatedField(queryset = User.objects.all())
    time = serializers.DateTimeField(input_formats=['%Y-%m-%dT%H:%M:%SZ'])

    def validate(self, data):

        where_tutor = Q(tutor = data['tutor'])
        where_appointee = Q(appointee = data['tutor'].username)
        appointments = Appointment.objects.filter(where_appointee | where_tutor)

        if data['time'] < datetime.now().replace(tzinfo=Eastern):
            raise serializers.ValidationError("Appointment can not be set in the past.")
        hour = timedelta(hours = 1)
        for app in appointments:
            if ((app.time + hour) > data['time']) and ((app.time + hour) < data['time'] + hour):
                raise serializers.ValidationError("Appointment already exists within the hour.")
        return data

    def create(self, validated_data):
        return Appointment(**validated_data)

    class Meta:
        model = Appointment
        exclude = []
        validators = [
            UniqueTogetherValidator(
                queryset=Appointment.objects.all(),
                fields = ['tutor', 'time'] )
                    ]
